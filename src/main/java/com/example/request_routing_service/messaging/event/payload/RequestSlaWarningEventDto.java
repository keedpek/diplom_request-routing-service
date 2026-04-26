package com.example.request_routing_service.messaging.event.payload;

import jakarta.validation.constraints.Min;
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
public class RequestSlaWarningEventDto {

  @NotNull(message = "Идентификатор заявки обязателен")
  private UUID requestId;

  @NotNull(message = "Идентификатор пользователя обязателен")
  private UUID userId;

  @NotBlank(message = "Заголовок заявки обязателен")
  @Size(max = 500, message = "Заголовок не длиннее 500 символов")
  private String title;

  @Min(value = 0, message = "Оставшиеся минуты не могут быть отрицательными")
  private Integer minutesLeft;

  private LocalDateTime deadline;
}
