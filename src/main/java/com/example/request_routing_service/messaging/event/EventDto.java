package com.example.request_routing_service.messaging.event;

import com.example.request_routing_service.enums.EventType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@JsonDeserialize(using = EventDtoDeserializer.class)
public class EventDto<T> {
  @NotNull(message = "Идентификатор события обязателен")
  private UUID eventId;

  @NotNull(message = "Тип события обязателен")
  private EventType eventType;

  @NotNull(message = "Данные о событии обязательны")
  private T payload;

  private LocalDateTime timestamp;
}
