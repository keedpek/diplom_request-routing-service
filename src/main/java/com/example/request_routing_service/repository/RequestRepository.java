package com.example.request_routing_service.repository;

import com.example.request_routing_service.entity.RequestEntity;
import com.example.request_routing_service.projection.RequestProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends Repository<RequestEntity, UUID> {

  @Query(value = """
    SELECT r.id as id,
           r.category_id as categoryId,
           r.priority as priority,
           r.deadline as deadline
    FROM requests r
    WHERE r.id = :id
""", nativeQuery = true)
  Optional<RequestProjection> findRequestById(UUID id);
}
