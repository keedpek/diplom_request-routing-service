package com.example.request_routing_service.messaging;

import com.example.request_routing_service.messaging.event.EventDto;
import com.example.request_routing_service.messaging.event.payload.RequestCreatedEventDto;
import com.example.request_routing_service.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventListeners {

  private final AssignmentService assignmentService;

  @KafkaListener(topics = "request.created")
  public void onRequestCreated(
          EventDto<RequestCreatedEventDto> event,
          Acknowledgment acknowledgment
  ) {
    log.info("Получено событие о создании запроса: eventId={}", event.getEventId());
    RequestCreatedEventDto payload = event.getPayload();
    log.debug("Информация о событии: {}", payload);
    assignmentService.assign(payload.getRequestId(), null);
    acknowledgment.acknowledge();
  }
}
