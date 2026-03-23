package com.example.request_routing_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "requests")
public class RequestEntity {
  @Id
  private UUID id;
}
