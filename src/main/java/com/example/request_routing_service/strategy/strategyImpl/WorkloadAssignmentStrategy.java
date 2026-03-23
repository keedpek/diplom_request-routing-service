package com.example.request_routing_service.strategy.strategyImpl;

import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("WORKLOAD")
public class WorkloadAssignmentStrategy implements AssignmentStrategy {
  @Override
  public Executor assign(List<Executor> candidates, double slaPressure) {
    return candidates.stream()
            .min(Comparator.comparing(Executor::getWorkLoad))
            .orElseThrow(() -> new RuntimeException("Что-то пошло не так"));
  }
}
