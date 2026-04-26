package com.example.request_routing_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class RequestDto {
  UUID id;
  Short categoryId;
  String priority;
  LocalDateTime deadline;
  UUID assignedToUserId;
}
