package com.example.request_routing_service.messaging.event.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSlaViolationEventDto implements EventDtoPayload{

  @NotNull(message = "Идентификатор заявки обязателен")
  private UUID requestId;

  @NotNull(message = "Идентификатор пользователя обязателен")
  private UUID userId;

  @NotBlank(message = "Заголовок заявки обязателен")
  @Size(max = 500, message = "Заголовок не длиннее 500 символов")
  private String title;

  private LocalDateTime deadline;

  private LocalDateTime violatedAt;
}
