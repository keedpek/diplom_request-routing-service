package com.example.request_routing_service.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Executor {
  private UUID userId;
  private String name;
  private double workLoad;
  private double successRate;
}
