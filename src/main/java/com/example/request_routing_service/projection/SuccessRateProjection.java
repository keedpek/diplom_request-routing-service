package com.example.request_routing_service.projection;

import java.util.UUID;

public interface SuccessRateProjection {
  UUID getUserId();
  Double getSuccessRate();
}
