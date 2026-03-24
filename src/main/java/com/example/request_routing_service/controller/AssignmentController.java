package com.example.request_routing_service.controller;

import com.example.request_routing_service.service.AssignmentService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/routing/assign")
@Validated
public class AssignmentController {
  private final AssignmentService assignmentService;

  @PostMapping("/{requestId}")
  public UUID assign(
          @PathVariable UUID requestId,
          @RequestParam(required = false, defaultValue = "WEIGHTED")
          @Pattern(
                  regexp = "^(WEIGHTED|SLA|WORKLOAD)$",
                  message = "Стратегия: WEIGHTED, SLA или WORKLOAD")
          String strategy
  ) {
    return assignmentService.assign(requestId, strategy);
  }
}
