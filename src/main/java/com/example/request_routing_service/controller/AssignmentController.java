package com.example.request_routing_service.controller;

import com.example.request_routing_service.DTO.AssignRequestDto;
import com.example.request_routing_service.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routing/assign")
public class AssignmentController {
  private final AssignmentService assignmentService;

  @PostMapping("/{requestId}")
  public UUID assign(
          @PathVariable UUID requestId,
          @Valid @RequestParam(required = false, defaultValue = "WEIGHTED") AssignRequestDto assignRequestDto
  ) {
    return assignmentService.assign(requestId, assignRequestDto);
  }
}
