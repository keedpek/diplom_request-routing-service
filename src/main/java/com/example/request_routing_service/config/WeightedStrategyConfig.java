package com.example.request_routing_service.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.strategies-weights.weighted")
public class WeightedStrategyConfig {
  private double loadWeight;
  private double performanceWeight;
  private double slaPressureWeight;

  @PostConstruct
  public void validate() {
    double sum = loadWeight + performanceWeight + slaPressureWeight;
    if (Math.abs(sum - 1.0) > 0.001) {
      throw new IllegalStateException("Некорректные значения весов, сумма должна быть равна 1, текущая: " + sum);
    }
  }
}
