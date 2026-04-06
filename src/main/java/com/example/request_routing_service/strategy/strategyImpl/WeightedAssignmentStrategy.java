package com.example.request_routing_service.strategy.strategyImpl;

import com.example.request_routing_service.config.WeightedStrategyConfig;
import com.example.request_routing_service.exceptions.AssignmentException;
import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("WEIGHTED")
@RequiredArgsConstructor
public class WeightedAssignmentStrategy implements AssignmentStrategy {
  private final WeightedStrategyConfig config;

  @Override
  public Executor assign(List<Executor> candidates, double slaPressure) {
    return candidates.stream()
            .min(Comparator.comparing(c -> calculateScore(c, slaPressure)))
            .orElseThrow(() -> new AssignmentException("Не удалось назначить исполнителя"));
  }

  private double calculateScore(Executor candidate, double slaPressure) {
    double load = candidate.getWorkLoad();
    double performance = candidate.getFailureRate();

    return load * config.getLoadWeight() + performance * config.getPerformanceWeight() + slaPressure * config.getSlaPressureWeight();
  }
}
