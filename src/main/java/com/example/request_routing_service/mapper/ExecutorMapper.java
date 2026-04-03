package com.example.request_routing_service.mapper;

import com.example.request_routing_service.DTO.ExecutorDto;
import com.example.request_routing_service.model.Executor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExecutorMapper {

  ExecutorDto toDto(Executor executor);
  Executor toEntity(ExecutorDto executorDto);
}
