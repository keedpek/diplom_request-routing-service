package com.example.request_routing_service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StrategyResolver {

  private final Map<String, AssignmentStrategy> strategies;

  public AssignmentStrategy getStrategy(String strategyName) {
    AssignmentStrategy strategy = strategies.get(strategyName);
    if (strategy == null) {
      throw new IllegalArgumentException("Неизвестная стратегия: " + strategyName + ". Доступные стратегии: " + strategies.keySet());
    }
    return strategy;
  }
}
