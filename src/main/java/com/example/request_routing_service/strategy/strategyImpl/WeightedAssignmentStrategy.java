package com.example.request_routing_service.strategy.strategyImpl;

import com.example.request_routing_service.config.WeightedStrategyConfig;
import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("WEIGHTED")
@RequiredArgsConstructor
public class WeightedAssignmentStrategy implements AssignmentStrategy {
  private final double MAX_SUCCESS_RATE = 1.0;

  private final WeightedStrategyConfig config;

  @Override
  public Executor assign(List<Executor> candidates, double slaPressure) {
    return candidates.stream()
            .min(Comparator.comparing(c -> calculateScore(c, slaPressure)))
            .orElseThrow(() -> new RuntimeException("Что-то пошло не так"));
  }

  private double calculateScore(Executor candidate, double slaPressure) {
    double load = candidate.getWorkLoad();
    double performance = MAX_SUCCESS_RATE - candidate.getSuccessRate();

    return load * config.getLoadWeight() + performance * config.getPerformanceWeight() + slaPressure * config.getSlaPressureWeight();
  }
}
