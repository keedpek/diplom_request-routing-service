package com.example.request_routing_service.service.serviceImpl;

import com.example.request_routing_service.DTO.AssignRequestDto;
import com.example.request_routing_service.DTO.ExecutorDto;
import com.example.request_routing_service.DTO.RequestDto;
import com.example.request_routing_service.exceptions.NotFoundException;
import com.example.request_routing_service.mapper.ExecutorMapper;
import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.repository.AssignmentJdbcRepository;
import com.example.request_routing_service.service.AssignmentService;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import com.example.request_routing_service.strategy.StrategyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

  private final double MIN_SLA_PRESSURE = 0.1;
  private final double MAX_SLA_PRESSURE = 1.0;

  private final AssignmentJdbcRepository assignmentJdbcRepository;
  private final StrategyResolver resolver;
  private final ExecutorMapper executorMapper;

  @Override
  public UUID assign(UUID requestId, AssignRequestDto assignRequestDto) {
    RequestDto requestDto = assignmentJdbcRepository.findRequestById(requestId);
    if (requestDto == null) { throw new NotFoundException("Запрос не найден"); }

    List<ExecutorDto> candidates = assignmentJdbcRepository.findCandidatesWithMetrics(requestDto.getCategoryId());

    if(candidates == null || candidates.isEmpty()) {
      throw new NotFoundException("Нет подходящих кандидатов");
    }

    double slaPressure = calculateSlaPressure(requestDto.getDeadline());

    List<Executor> executors = candidates.stream().map(executorMapper::toEntity).toList();

    AssignmentStrategy strategy = resolver.getStrategy(assignRequestDto.getStrategy().toUpperCase());
    return strategy.assign(executors, slaPressure).getUserId();
  }

  private double calculateSlaPressure(LocalDateTime deadline) {
    if (deadline == null) return MIN_SLA_PRESSURE;
    long minutesUntilDeadline = Duration.between(LocalDateTime.now(), deadline).toMinutes();
    if (minutesUntilDeadline <= 0) return MAX_SLA_PRESSURE;
    return 1.0 / minutesUntilDeadline;
  }
}
