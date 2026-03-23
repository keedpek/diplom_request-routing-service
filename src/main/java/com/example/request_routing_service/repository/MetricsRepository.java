package com.example.request_routing_service.repository;

import com.example.request_routing_service.entity.RequestEntity;
import com.example.request_routing_service.projection.LoadProjection;
import com.example.request_routing_service.projection.SuccessRateProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

//TODO: filter by department
public interface MetricsRepository extends Repository<RequestEntity, UUID> {

  @Query(value = """
        SELECT r.assigned_to_user_id as userId,
               COUNT(r) as load
        FROM requests r
        WHERE r.status IN ('NEW', 'ASSIGNED', 'IN_PROGRESS')
        GROUP BY r.assigned_to_user_id
    """, nativeQuery = true)
  List<LoadProjection> getCandidatesLoad();

  @Query(value = """
        SELECT r.assigned_to_user_id as userId,
               COUNT(r) FILTER (
                   WHERE r.deadline IS NOT NULL AND r.updated_at <= r.deadline
               ) * 1.0 / COUNT(r) as successRate
        FROM requests r
        WHERE r.status IN ('WAITING_FOR_RESPONSE', 'COMPLETED', 'CANCELLED')
        GROUP BY r.assigned_to_user_id
    """, nativeQuery = true)
  List<SuccessRateProjection> getSuccessRate();
}
