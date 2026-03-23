package com.example.request_routing_service.projection;

import java.util.UUID;

public interface LoadProjection {
  UUID getUserId();
  int getLoad();
}
