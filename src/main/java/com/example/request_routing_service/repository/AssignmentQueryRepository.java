package com.example.request_routing_service.repository;

import com.example.request_routing_service.entity.RequestEntity;
import com.example.request_routing_service.projection.CandidateProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface AssignmentQueryRepository extends Repository<RequestEntity, UUID> {
  @Query(value = """
        SELECT cd.department_id
        FROM category_departments cd
        WHERE cd.category_id = :categoryId
    """, nativeQuery = true)
  List<Short> findDepartmentsByCategory(Short categoryId);

  @Query(value = """
        SELECT u.id as userId,
               up.first_name as firstName,
               up.last_name as lastName
        FROM users u
        JOIN user_profiles up ON u.id = up.user_id
        WHERE up.department_id IN :departmentIds
    """, nativeQuery = true)
  List<CandidateProjection> findCandidates(List<Short> departmentIds);
}
