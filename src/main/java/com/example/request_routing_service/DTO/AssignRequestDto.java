package com.example.request_routing_service.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AssignRequestDto {
  @Pattern(
          regexp = "(?i)^(WEIGHTED|SLA|WORKLOAD)$",
          message = "Стратегия: WEIGHTED, SLA или WORKLOAD"
  )
  private String strategy;
}
