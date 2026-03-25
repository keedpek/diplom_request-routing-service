package com.example.request_routing_service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StrategyResolver {

  private final Map<String, AssignmentStrategy> strategies;

  public AssignmentStrategy getStrategy(String strategyName) {
    StringBuilder sb = new StringBuilder();
    AssignmentStrategy strategy = strategies.get(strategyName);
    if (strategy == null) {
      throw new IllegalArgumentException(sb.append("Неизвестная стратегия: ")
              .append(strategyName)
              .append(". Доступные стратегии: ")
              .append(strategies.keySet())
              .toString()
      );
    }
    return strategy;
  }
}
