package com.example.request_routing_service.strategy.strategyImpl;

import com.example.request_routing_service.exceptions.AssignmentException;
import com.example.request_routing_service.model.Executor;
import com.example.request_routing_service.strategy.AssignmentStrategy;
import com.example.request_routing_service.util.AssignmentConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("SLA")
@RequiredArgsConstructor
public class SlaAssignmentStrategy implements AssignmentStrategy {

  @Override
  public Executor assign(List<Executor> candidates, double slaPressure) {
    return candidates.stream()
            .min(Comparator.comparing(c -> score(c, slaPressure)))
            .orElseThrow(() -> new AssignmentException("Не удалось назначить исполнителя"));
  }

  private double score(Executor candidate, double slaPressure) {
    double queryTime = candidate.getWorkLoad() * candidate.getAvgTaskTime();
    double executionTime = candidate.getAvgTaskTime();
    double eta = queryTime + executionTime;

    double risk = candidate.getFailureRate() * eta;

    return eta * slaPressure + risk * AssignmentConstants.RISK_WEIGHT;
  }
}
