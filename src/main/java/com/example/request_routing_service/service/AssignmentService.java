package com.example.request_routing_service.service;

import java.util.UUID;

public interface AssignmentService {
  UUID assign(UUID requestId, String strategy);
}
