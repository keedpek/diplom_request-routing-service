package com.example.request_routing_service.exceptions;

import com.example.request_routing_service.DTO.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponseDto handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpServletRequest request
  ) {
    StringBuilder messageBuilder = new StringBuilder();

    ex.getBindingResult().getFieldErrors().forEach(error -> {
      if (!messageBuilder.isEmpty()) messageBuilder.append("; ");

      messageBuilder.append(error.getField())
              .append(": ")
              .append(error.getDefaultMessage());
    });

    return ErrorResponseDto.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(messageBuilder.toString())
            .path(request.getRequestURI())
            .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ErrorResponseDto handleConstraintViolation(
          ConstraintViolationException ex,
          HttpServletRequest request
  ) {
    StringBuilder messageBuilder = new StringBuilder();

    ex.getConstraintViolations().forEach(error -> {
      if (!messageBuilder.isEmpty()) messageBuilder.append("; ");

      messageBuilder.append(error.getPropertyPath())
              .append(": ")
              .append(error.getMessage());
    });

    return ErrorResponseDto.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(messageBuilder.toString())
            .path(request.getRequestURI())
            .build();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ErrorResponseDto handleNotFoundException(
          NotFoundException ex,
          HttpServletRequest request
  ) {
    return ErrorResponseDto.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorResponseDto handleIllegalArgumentException(
          IllegalArgumentException ex,
          HttpServletRequest request
  ) {
    return ErrorResponseDto.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(AssignmentException.class)
  public ErrorResponseDto handleAssignmentException(
          AssignmentException ex,
          HttpServletRequest request
  ) {
    return ErrorResponseDto.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
  }
}
