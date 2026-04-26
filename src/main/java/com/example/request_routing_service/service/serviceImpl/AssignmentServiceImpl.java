package com.example.request_routing_service.service.serviceImpl;

import com.example.request_routing_service.DTO.AssignRequestDto;
import com.example.request_routing_service.DTO.ExecutorDto;
import com.example.request_routing_service.DTO.RequestDto;
import com.example.request_routing_service.enums.EventType;
import com.example.request_routing_service.exceptions.NotFoundException;
import com.example.request_routing_service.mapper.ExecutorMapper;
import com.example.request_routing_service.messaging.event.EventDto;
import com.example.request_routing_service.messaging.event.payload.ExecutorFoundEventDto;
import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.repository.AssignmentJdbcRepository;
import com.example.request_routing_service.service.AssignmentService;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import com.example.request_routing_service.strategy.StrategyResolver;
import com.example.request_routing_service.util.AssignmentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

  private final AssignmentJdbcRepository assignmentJdbcRepository;
  private final StrategyResolver resolver;
  private final ExecutorMapper executorMapper;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public UUID assign(UUID requestId, AssignRequestDto assignRequestDto) {
    String strategyTitle = assignRequestDto != null ? assignRequestDto.getStrategy() : "WEIGHTED";
    log.info(
            "Начало назначения: requestId={}, strategy={}",
            requestId,
            strategyTitle
    );

    RequestDto requestDto = assignmentJdbcRepository.findRequestById(requestId);
    if (requestDto == null) {
      log.warn("Заявка не найдена: requestId={}", requestId);
      throw new NotFoundException("Заявка не найдена");
    }

    if(requestDto.getAssignedToUserId() != null) {
      return null;
    }

    List<ExecutorDto> candidates = assignmentJdbcRepository.findCandidatesWithMetrics(requestDto.getCategoryId());

    if(candidates == null || candidates.isEmpty()) {
      log.warn("Нет кандидатов: categoryId={}", requestDto.getCategoryId());
      throw new NotFoundException("Нет подходящих кандидатов");
    }
    log.debug("Найдено кандидатов: count={}", candidates.size());

    double slaPressure = calculateSlaPressure(requestDto.getDeadline());
    log.debug("SLA pressure: {}", slaPressure);

    List<Executor> executors = candidates.stream().map(executorMapper::toEntity).toList();

    AssignmentStrategy strategy = resolver.getStrategy(strategyTitle.toUpperCase());
    UUID executorId = strategy.assign(executors, slaPressure).getUserId();
    log.info("Назначен исполнитель: requestId={}, executorId={}", requestId, executorId);
    EventDto<ExecutorFoundEventDto> event = EventDto.<ExecutorFoundEventDto>builder()
            .eventId(UUID.randomUUID())
            .eventType(EventType.EXECUTOR_FOUND)
            .timestamp(LocalDateTime.now())
            .payload(ExecutorFoundEventDto.builder()
                    .requestId(requestId)
                    .assignedUserId(executorId)
                    .build())
            .build();
    kafkaTemplate.send("request.executor.found", event);
    return executorId;
  }

  private double calculateSlaPressure(LocalDateTime deadline) {
    if (deadline == null) return AssignmentConstants.MIN_SLA_PRESSURE;
    long minutesUntilDeadline = Duration.between(LocalDateTime.now(), deadline).toMinutes();
    if (minutesUntilDeadline <= 0) return AssignmentConstants.MAX_SLA_PRESSURE;
    return 1.0 / minutesUntilDeadline;
  }
}
