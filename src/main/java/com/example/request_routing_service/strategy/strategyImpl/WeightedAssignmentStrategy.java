package com.example.request_routing_service.strategy.strategyImpl;

import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("WEIGHTED")
public class WeightedAssignmentStrategy implements AssignmentStrategy {

  @Override
  public Executor assign(List<Executor> candidates, double slaPressure) {
    return candidates.stream()
            .min(Comparator.comparing(c -> calculateScore(c, slaPressure)))
            .orElseThrow(() -> new RuntimeException("Что-то пошло не так"));
  }

  //TODO: mb skill diff
  private double calculateScore(Executor candidate, double slaPressure) {
    double load = candidate.getWorkLoad();
    double performance = 1 - candidate.getSuccessRate();

    return load * 0.5 + performance * 0.3 + slaPressure * 0.2;
  }
}
