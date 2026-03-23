package com.example.request_routing_service.projection;

import java.util.UUID;

public interface CandidateProjection {
  UUID getUserId();
  String getFirstName();
  String getLastName();
}
