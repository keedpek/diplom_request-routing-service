package com.example.request_routing_service.messaging.event.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatusChangedEventDto {

  @NotNull(message = "Идентификатор заявки обязателен")
  private UUID requestId;

  private List<UUID> userIds;

  @NotBlank(message = "Статус обязателен") 
  @Pattern(
    regexp = "^(NEW|ASSIGNED|IN_PROGRESS|WAITING_FOR_RESPONSE|COMPLETED|CANCELLED)$",
    message = "Некорректный статус заявки"
  )
  private String status;
}
