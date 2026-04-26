package com.example.request_routing_service.messaging.event.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorFoundEventDto {
  @NotNull(message = "Идентификатор заявки обязателен")
  private UUID requestId;

  @NotNull(message = "Идентификатор исполнителя обязателен")
  private UUID assignedUserId;
}
