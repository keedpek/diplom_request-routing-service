package com.example.request_routing_service.strategy.strategyImpl;

import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("SLA")
public class SlaAssignmentStrategy implements AssignmentStrategy {
  private final double MIN_RISK = 1.0;
  private final double MAX_RISK = 5.0;
  private final double MAX_SUCCESS_RATE = 1.0;

  @Override
  public Executor assign(List<Executor> candidates, double slaPressure) {
    return candidates.stream()
            .min(Comparator.comparing(c -> eta(c, slaPressure)))
            .orElseThrow(() -> new RuntimeException("Что-то пошло не так"));
  }

  private double eta(Executor candidate, double slaPressure) {
    double base = candidate.getWorkLoad();
    double risk = MIN_RISK + (MAX_SUCCESS_RATE - candidate.getSuccessRate()) * (MAX_RISK - MIN_RISK);
    return base * risk * slaPressure;
  }
}
