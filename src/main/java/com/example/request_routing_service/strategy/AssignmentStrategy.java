package com.example.request_routing_service.strategy;

import com.example.request_routing_service.model.Executor;

import java.util.List;

public interface AssignmentStrategy {
  Executor assign(List<Executor> candidates, double slaPressure);
}
