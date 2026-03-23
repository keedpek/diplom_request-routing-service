package com.example.request_routing_service.controller;

import com.example.request_routing_service.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/routing/assign")
public class AssignmentController {
  private final AssignmentService assignmentService;

  @PostMapping("/{requestId}")
  public UUID assign(@PathVariable UUID requestId) {
    return assignmentService.assign(requestId, "WEIGHTED");
  }
}
