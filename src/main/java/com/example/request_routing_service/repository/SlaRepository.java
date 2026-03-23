package com.example.request_routing_service.repository;

import com.example.request_routing_service.entity.RequestEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.UUID;

public interface SlaRepository extends Repository<RequestEntity, UUID> {
  @Query(value = """
        SELECT s.execution_time_minutes
        FROM sla_rules s
        WHERE s.category_id = :categoryId
        AND s.priority = :priority
    """, nativeQuery = true)
  Integer getExecutionTime(Short categoryId, String priority);
}
