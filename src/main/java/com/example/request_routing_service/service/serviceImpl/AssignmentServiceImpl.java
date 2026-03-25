package com.example.request_routing_service.service.serviceImpl;

import com.example.request_routing_service.exceptions.NotFoundException;
import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.projection.CandidateProjection;
import com.example.request_routing_service.projection.LoadProjection;
import com.example.request_routing_service.projection.RequestProjection;
import com.example.request_routing_service.projection.SuccessRateProjection;
import com.example.request_routing_service.repository.AssignmentQueryRepository;
import com.example.request_routing_service.repository.MetricsRepository;
import com.example.request_routing_service.repository.RequestRepository;
import com.example.request_routing_service.repository.SlaRepository;
import com.example.request_routing_service.service.AssignmentService;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import com.example.request_routing_service.strategy.StrategyResolver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

  private final RequestRepository requestRepository;
  private final AssignmentQueryRepository assignmentQueryRepository;
  private final MetricsRepository metricsRepository;
  private final SlaRepository slaRepository;
  private final StrategyResolver resolver;

  @Override
  @Transactional
  public UUID assign(UUID requestId, String strategyType) {
    RequestProjection request = requestRepository.findRequestById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));

    List<Short> departments = assignmentQueryRepository.findDepartmentsByCategory(request.getCategoryId());

    List<CandidateProjection> departmentCandidates = assignmentQueryRepository.findCandidates(departments);

    if(departmentCandidates == null || departmentCandidates.isEmpty()) {
      throw new NotFoundException("Нет подходящих кандидатов");
    }

    Map<UUID, Integer> loadMap = metricsRepository.getCandidatesLoad().stream()
            .collect(Collectors.toMap(
                    LoadProjection::getUserId,
                    LoadProjection::getLoad
            ));

    Map<UUID, Double> successMap = metricsRepository.getSuccessRate().stream()
            .collect(Collectors.toMap(
                    SuccessRateProjection::getUserId,
                    SuccessRateProjection::getSuccessRate
            ));

    int slaTime = slaRepository.getExecutionTime(
            request.getCategoryId(),
            request.getPriority()
    );
    double slaPressure = calculateSlaPressure(request.getDeadline(), slaTime);

    List<Executor> candidates = departmentCandidates.stream()
            .map(c -> enrichWithMetrics(c, loadMap, successMap))
            .toList();

    AssignmentStrategy strategy = resolver.getStrategy(strategyType);
    return strategy.assign(candidates, slaPressure).getUserId();
  }

  private Executor enrichWithMetrics(
          CandidateProjection candidate,
          Map<UUID, Integer> loadMap,
          Map<UUID, Double> successMap
  ) {
    int workload = loadMap.getOrDefault(candidate.getUserId(), 0);
    Double successRate = successMap.getOrDefault(candidate.getUserId(), 0.8);

    return Executor.builder()
            .userId(candidate.getUserId())
            .name(candidate.getFirstName() + " " + candidate.getLastName())
            .workLoad(workload)
            .successRate(successRate)
            .build();
  }

  private double calculateSlaPressure(LocalDateTime deadline, int slaTime) {
    if (deadline == null) return 0.1;
    long minutesUntilDeadline = Duration.between(LocalDateTime.now(), deadline).toMinutes();
    if (minutesUntilDeadline <= 0) return 10;
    return (double) slaTime / minutesUntilDeadline;
  }
}
