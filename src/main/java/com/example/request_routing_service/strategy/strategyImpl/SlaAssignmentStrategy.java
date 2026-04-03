package com.example.request_routing_service.strategy.strategyImpl;

import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("SLA")
@RequiredArgsConstructor
public class SlaAssignmentStrategy implements AssignmentStrategy {
  private final double RISK_WEIGHT = 0.3;

  @Override
  public Executor assign(List<Executor> candidates, double slaPressure) {
    return candidates.stream()
            .min(Comparator.comparing(c -> score(c, slaPressure)))
            .orElseThrow(() -> new RuntimeException("Что-то пошло не так"));
  }

  private double score(Executor candidate, double slaPressure) {
    double queryTime = candidate.getWorkLoad() * candidate.getAvgTaskTime();
    double executionTime = candidate.getAvgTaskTime();
    double eta = queryTime + executionTime;

    double risk = candidate.getFailureRate() * eta;

    return eta * slaPressure + risk * RISK_WEIGHT;
  }
}
