package com.example.request_routing_service.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RequestProjection {
  UUID getId();
  Short getCategoryId();
  String getPriority();
  LocalDateTime getDeadline();
}
