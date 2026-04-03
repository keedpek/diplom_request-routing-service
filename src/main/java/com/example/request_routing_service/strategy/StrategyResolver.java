package com.example.request_routing_service.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyResolver {

  private final Map<String, AssignmentStrategy> strategies;

  public AssignmentStrategy getStrategy(String strategyName) {
    log.debug("Поиск стратегии: {}", strategyName);

    StringBuilder sb = new StringBuilder();
    AssignmentStrategy strategy = strategies.get(strategyName);
    if (strategy == null) {
      log.error("Неизвестная стратегия: {}", strategyName);
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
