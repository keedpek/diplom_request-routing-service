package com.example.request_routing_service.repository;

import com.example.request_routing_service.DTO.ExecutorDto;
import com.example.request_routing_service.DTO.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AssignmentJdbcRepository {
  private final JdbcTemplate jdbcTemplate;

  public RequestDto findRequestById(UUID requestId) {
    String sql = """
            SELECT
              r.id,
              r.category_id,
              r.priority,
              r.deadline,
              r.assigned_to_user_id
            FROM requests r
            WHERE r.id = ?
            """;

    return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new RequestDto(
            rs.getObject("id", UUID.class),
            rs.getShort("category_id"),
            rs.getString("priority"),
            rs.getTimestamp("deadline") != null
                    ? rs.getTimestamp("deadline").toLocalDateTime()
                    : null,
            rs.getObject("assigned_to_user_id", UUID.class)
    ), requestId);
  }

  public List<ExecutorDto> findCandidatesWithMetrics(Short categoryId) {
    String sql = """
        SELECT
            u.id as user_id,
            up.first_name,
            up.last_name,
            COALESCE(load.load, 0) as work_load,
            COALESCE(sr.success_rate, 0.8) as success_rate,
            COALESCE(avg_time.avg_time, 30) as avg_task_time
        FROM users u
        JOIN user_profiles up ON u.id = up.user_id
        JOIN category_departments cd ON cd.department_id = up.department_id
        
        LEFT JOIN (
            SELECT
                r.assigned_to_user_id as user_id,
                COUNT(*) as load
            FROM requests r
            WHERE r.status IN ('NEW', 'ASSIGNED', 'IN_PROGRESS')
            GROUP BY r.assigned_to_user_id
        ) load ON load.user_id = u.id
        
        LEFT JOIN (
            SELECT
                r.assigned_to_user_id as user_id,
                COUNT(*) FILTER (
                    WHERE r.deadline IS NOT NULL
                    AND r.updated_at <= r.deadline
                ) * 1.0 / COUNT(*) as success_rate
            FROM requests r
            WHERE r.status IN ('COMPLETED', 'CANCELLED')
            GROUP BY r.assigned_to_user_id
        ) sr ON sr.user_id = u.id
        
        LEFT JOIN (
            SELECT
                r.assigned_to_user_id as user_id,
                AVG(EXTRACT(EPOCH FROM (r.updated_at - r.created_at)) / 60) as avg_time
            FROM requests r
            WHERE r.status = 'COMPLETED'
            GROUP BY r.assigned_to_user_id
        ) avg_time ON avg_time.user_id = u.id
        
        WHERE cd.category_id = ?
    """;

    return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> {
              StringBuilder name = new StringBuilder();
              name.append(rs.getString("first_name"))
                      .append(" ")
                      .append(rs.getString("last_name"));

              return new ExecutorDto(
                      rs.getObject("user_id", UUID.class),
                      name.toString(),
                      rs.getInt("work_load"),
                      rs.getDouble("success_rate"),
                      rs.getDouble("avg_task_time")
              );
            },
            categoryId
    );
  }
}
