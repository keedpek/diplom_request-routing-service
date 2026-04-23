package com.example.request_routing_service.service;

import com.example.request_routing_service.DTO.AssignRequestDto;

import java.util.UUID;

public interface AssignmentService {
  UUID assign(UUID requestId, AssignRequestDto assignRequestDto);
}
