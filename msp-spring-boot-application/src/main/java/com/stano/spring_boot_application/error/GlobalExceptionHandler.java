package com.stano.spring_boot_application.error;

import com.stano.exceptions.BadRequestException;
import com.stano.exceptions.ForbiddenException;
import com.stano.exceptions.InternalServerError;
import com.stano.exceptions.InvalidRequestException;
import com.stano.exceptions.ReflectionException;
import com.stano.exceptions.ResourceConflictException;
import com.stano.exceptions.ResourceLockedException;
import com.stano.exceptions.ResourceNotFoundException;
import com.stano.exceptions.RuntimeIOException;
import com.stano.exceptions.RuntimeMalformedURLException;
import com.stano.exceptions.RuntimeSQLException;
import com.stano.exceptions.ServiceUnavailableException;
import com.stano.exceptions.UnauthorizedException;
import com.stano.logging.SemanticLogger;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Instant;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final String GENERIC_DETAIL =
      "An unexpected error occurred. Please contact support if the problem persists.";

  private static final SemanticLogger logger =
      SemanticLogger.using(LoggerFactory.getLogger(GlobalExceptionHandler.class));

  @ExceptionHandler({BadRequestException.class, InvalidRequestException.class})
  public ProblemDetail handleBadRequest(RuntimeException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.BAD_REQUEST, ex, request);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ProblemDetail handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.UNAUTHORIZED, ex, request);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ProblemDetail handleForbidden(ForbiddenException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.FORBIDDEN, ex, request);
  }

  @ExceptionHandler({
    ResourceNotFoundException.class,
    EntityNotFoundException.class,
    JpaObjectRetrievalFailureException.class
  })
  public ProblemDetail handleNotFound(RuntimeException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.NOT_FOUND, ex, request);
  }

  @ExceptionHandler(ResourceConflictException.class)
  public ProblemDetail handleConflict(ResourceConflictException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.CONFLICT, ex, request);
  }

  @ExceptionHandler(ResourceLockedException.class)
  public ProblemDetail handleLocked(ResourceLockedException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.LOCKED, ex, request);
  }

  @ExceptionHandler(ServiceUnavailableException.class)
  public ProblemDetail handleServiceUnavailable(
      ServiceUnavailableException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.SERVICE_UNAVAILABLE, ex, request);
  }

  @ExceptionHandler({
    ReflectionException.class,
    RuntimeIOException.class,
    RuntimeMalformedURLException.class,
    RuntimeSQLException.class,
    InternalServerError.class
  })
  public ProblemDetail handleInternalError(RuntimeException ex, HttpServletRequest request) {
    return handleServerError(ex, request);
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest request) {
    return handleServerError(ex, request);
  }

  private ProblemDetail handleClientError(
      HttpStatus status, RuntimeException ex, HttpServletRequest request) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    problemDetail.setTitle(status.getReasonPhrase());
    enrichWithContext(problemDetail, request);

    logger
        .with("status", status.value())
        .with("path", request.getRequestURI())
        .warn("Handled {} for request {}", ex.getClass().getSimpleName(), request.getRequestURI());

    return problemDetail;
  }

  private ProblemDetail handleServerError(Exception ex, HttpServletRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_DETAIL);
    problemDetail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    enrichWithContext(problemDetail, request);

    logger
        .with("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
        .with("path", request.getRequestURI())
        .error(ex, "Unhandled exception handling request {}", request.getRequestURI());

    return problemDetail;
  }

  private void enrichWithContext(ProblemDetail problemDetail, HttpServletRequest request) {
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    String traceId = MDC.get("traceId");
    if (traceId != null) {
      problemDetail.setProperty("traceId", traceId);
    }

    problemDetail.setProperty("timestamp", Instant.now());
  }
}
