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

/**
 * {@code @RestControllerAdvice} that maps msp-common's exception hierarchy (and a handful of common
 * framework exceptions) to RFC 7807 {@link ProblemDetail} HTTP responses.
 *
 * <p>Each handler logs the failure via {@link SemanticLogger} with the resolved HTTP status and
 * request path, and every response is enriched with the request's {@code traceId} (from MDC, if
 * present) and a {@code timestamp}. Unexpected exceptions are mapped to a generic {@code 500}
 * response so internal details are never leaked to callers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final String GENERIC_DETAIL =
      "An unexpected error occurred. Please contact support if the problem persists.";

  private static final SemanticLogger logger =
      SemanticLogger.using(LoggerFactory.getLogger(GlobalExceptionHandler.class));

  /**
   * Maps {@link BadRequestException} and {@link InvalidRequestException} to a {@code 400 Bad
   * Request} response.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail describing the bad request
   */
  @ExceptionHandler({BadRequestException.class, InvalidRequestException.class})
  public ProblemDetail handleBadRequest(RuntimeException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.BAD_REQUEST, ex, request);
  }

  /**
   * Maps {@link UnauthorizedException} to a {@code 401 Unauthorized} response.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail describing the authentication failure
   */
  @ExceptionHandler(UnauthorizedException.class)
  public ProblemDetail handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.UNAUTHORIZED, ex, request);
  }

  /**
   * Maps {@link ForbiddenException} to a {@code 403 Forbidden} response.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail describing the authorization failure
   */
  @ExceptionHandler(ForbiddenException.class)
  public ProblemDetail handleForbidden(ForbiddenException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.FORBIDDEN, ex, request);
  }

  /**
   * Maps {@link ResourceNotFoundException}, {@link EntityNotFoundException}, and {@link
   * JpaObjectRetrievalFailureException} to a {@code 404 Not Found} response.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail describing the missing resource
   */
  @ExceptionHandler({
    ResourceNotFoundException.class,
    EntityNotFoundException.class,
    JpaObjectRetrievalFailureException.class
  })
  public ProblemDetail handleNotFound(RuntimeException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.NOT_FOUND, ex, request);
  }

  /**
   * Maps {@link ResourceConflictException} to a {@code 409 Conflict} response.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail describing the conflict
   */
  @ExceptionHandler(ResourceConflictException.class)
  public ProblemDetail handleConflict(ResourceConflictException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.CONFLICT, ex, request);
  }

  /**
   * Maps {@link ResourceLockedException} to a {@code 423 Locked} response.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail describing the locked resource
   */
  @ExceptionHandler(ResourceLockedException.class)
  public ProblemDetail handleLocked(ResourceLockedException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.LOCKED, ex, request);
  }

  /**
   * Maps {@link ServiceUnavailableException} to a {@code 503 Service Unavailable} response.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail describing the unavailable service
   */
  @ExceptionHandler(ServiceUnavailableException.class)
  public ProblemDetail handleServiceUnavailable(
      ServiceUnavailableException ex, HttpServletRequest request) {
    return handleClientError(HttpStatus.SERVICE_UNAVAILABLE, ex, request);
  }

  /**
   * Maps {@link ReflectionException}, {@link RuntimeIOException}, {@link
   * RuntimeMalformedURLException}, {@link RuntimeSQLException}, and {@link InternalServerError} to
   * a generic {@code 500 Internal Server Error} response, logging the underlying exception at error
   * level.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail with a generic message; the original exception message is not exposed
   *     to the caller
   */
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

  /**
   * Fallback handler for any exception not covered by a more specific handler. Maps to a generic
   * {@code 500 Internal Server Error} response, logging the underlying exception at error level.
   *
   * @param ex the exception that was thrown
   * @param request the current request, used for path and trace enrichment
   * @return a problem detail with a generic message; the original exception message is not exposed
   *     to the caller
   */
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
