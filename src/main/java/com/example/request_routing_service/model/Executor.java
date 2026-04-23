package com.example.request_routing_service.model;

import com.example.request_routing_service.util.AssignmentConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Executor {
  private UUID userId;
  private String name;
  private int workLoad;
  private double successRate;
  private double avgTaskTime;

  public double getFailureRate() {
    return AssignmentConstants.MAX_SUCCESS_RATE - successRate;
  }
}
