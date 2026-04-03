package com.example.request_routing_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ExecutorDto {
  private UUID userId;
  private String name;
  private int workLoad;
  private double successRate;
  private double avgTaskTime;
}
