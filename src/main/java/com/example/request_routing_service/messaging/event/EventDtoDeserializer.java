package com.example.request_routing_service.messaging.event;

import com.example.request_routing_service.enums.EventType;
import com.example.request_routing_service.messaging.event.payload.*;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class EventDtoDeserializer extends JsonDeserializer<EventDto<? extends EventDtoPayload>> {

  @Override
  public EventDto<? extends EventDtoPayload> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
    ObjectNode node = jsonParser.readValueAsTree();
    String eventType = node.get("eventType").asText();
    ObjectNode payload = (ObjectNode) node.get("payload");

    Class<? extends EventDtoPayload> payloadClass = switch (eventType) {
      case "REQUEST_CREATED" -> RequestCreatedEventDto.class;
      case "EXECUTOR_FOUND" -> ExecutorFoundEventDto.class;
      case "REQUEST_ASSIGNED" -> RequestAssignedEventDto.class;
      case "STATUS_CHANGED" -> RequestStatusChangedEventDto.class;
      case "SLA_WARNING" -> RequestSlaWarningEventDto.class;
      case "SLA_VIOLATION" -> RequestSlaViolationEventDto.class;
      default -> throw new IllegalArgumentException("Некорректный event type: " + eventType);
    };

    EventDtoPayload payloadObject = deserializationContext.readTreeAsValue(payload, payloadClass);
    LocalDateTime timestamp = deserializationContext.readTreeAsValue(node.get("timestamp"), LocalDateTime.class);

    return EventDto.<EventDtoPayload>builder()
            .eventId(UUID.fromString(node.get("eventId").asText()))
            .eventType(EventType.valueOf(eventType))
            .timestamp(timestamp)
            .payload(payloadObject)
            .build();
  }
}
