package com.mds.error.handler.exception.handler;

import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import com.mds.error.handler.exception.GeneralException;
import com.mds.error.handler.exception.customizer.ErrorBuilderCustomizer;
import com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper;
import com.mds.error.handler.config.ErrorMessages;
import com.mds.error.handler.exception.keys.ExceptionMessageKeys;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.model.response.ErrorResponse;
import com.mds.error.handler.utils.CacheControlUtils;
import com.mds.error.handler.utils.ErrorUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global {@link RestControllerAdvice} that intercepts all exceptions thrown
 * by Spring MVC controllers and converts them into a standardised
 * {@link ErrorResponse}.
 *
 * <p>Processing order:
 * <ol>
 *   <li>{@link GeneralException} and its subclasses are handled directly,
 *       preserving the error code, message, action, and HTTP status.</li>
 *   <li>All other {@link Exception} types are routed through the registered
 *       {@link ExceptionResolver} chain; the first resolver that
 *       {@linkplain ExceptionResolver#supports(Exception) supports} the
 *       exception builds the response.</li>
 *   <li>If no resolver matches, a generic {@code 500} response is returned.</li>
 * </ol>
 *
 * <p>Before returning, every response is passed through all registered
 * {@link ErrorBuilderCustomizer} implementations for optional enrichment
 * (e.g. adding trace IDs or masking sensitive data).
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Order(Integer.MIN_VALUE)
@RestControllerAdvice
public class ErrorExceptionHandler {

  private final ErrorUtils errorUtils;
  private final List<ExceptionResolver<?>> resolvers;
  private final List<ErrorBuilderCustomizer> errorBuilderCustomizers;

  /**
   * Constructor for ErrorExceptionHandler.
   *
   * @param errorUtils              Utility class for error handling.
   * @param resolvers               List of resolvers to handle specific exceptions.
   * @param errorBuilderCustomizers List of customizers to modify error responses.
   */
  public ErrorExceptionHandler(ErrorUtils errorUtils,
                               List<ExceptionResolver<?>> resolvers,
                               List<ErrorBuilderCustomizer> errorBuilderCustomizers) {

    this.errorUtils = errorUtils;
    this.resolvers = resolvers;
    this.errorBuilderCustomizers = errorBuilderCustomizers;
  }

  /**
   * Handles GeneralException and builds a response.
   *
   * @param exception The GeneralException to handle.
   * @return ResponseEntity containing the error response.
   */
  @ExceptionHandler(GeneralException.class)
  public ResponseEntity<Object> handleGeneralException(GeneralException exception) {
    log.info("[ErrorExceptionHandler] - Handling GeneralException: code={}, message={}", exception.getCode(), exception.getMessage());

    return buildResponseError(ErrorExceptionHandlerHelper.createError(exception,
                                                                      exception.getAction(),
                                                                      errorUtils.getType(exception),
                                                                      exception.getHttpStatusCode(),
                                                                      exception.getTitle(),
                                                                      exception.getCode(),
                                                                      exception.getMessage()));
  }

  /**
   * Handles generic exceptions and attempts to resolve them using registered resolvers.
   *
   * <p>This method is invoked when an unhandled exception occurs. It iterates through the list of
   * {@link ExceptionResolver} instances to determine if any resolver supports the given exception.
   * If a resolver is found, the exception is converted and resolved into an {@link ErrorResponse}.
   * If no resolver supports the exception, a generic error response is returned.
   *
   * <p>Steps:
   * 1. Log the unhandled exception. 2. Iterate through the list of resolvers to find one
   * that supports the exception.
   * 3. If a resolver is found:
   *    - Cast the resolver to a typed resolver (`ExceptionResolver<Exception>`).
   *    - Convert the exception to a typed exception using the resolver.
   *    - Resolve the typed exception into an error response.
   *    - Return the built error response.
   * 4. If no resolver supports the exception, return a generic error response.
   *
   * @param exception The exception to handle.
   * @return A {@link ResponseEntity} containing the resolved or generic error response.
   */
  @SuppressWarnings("unchecked")
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception exception) {
    log.error("[ErrorExceptionHandler] - Unhandled exception caught: {}", exception.getMessage(), exception);

    for (ExceptionResolver<?> resolver : resolvers) {
      if (resolver.supports(exception)) {
        log.debug("[ErrorExceptionHandler] - Exception resolved by: {}", resolver.getClass().getSimpleName());
        ExceptionResolver<Exception> typedResolver = (ExceptionResolver<Exception>) resolver;
        Exception exceptionResolver = typedResolver.convertToTypedException(exception);
        return buildResponseError(typedResolver.resolve(exceptionResolver));
      }
    }

    log.warn("[ErrorExceptionHandler] - Exception not resolved, returning generic error.");
    return buildResponseError(ErrorExceptionHandlerHelper.createError(exception,
                                                                      Action.BACK_HOME,
                                                                      Type.TECHNICAL,
                                                                      HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                                      ErrorMessages.resolve(ExceptionMessageKeys.DEFAULT_ERROR_TITLE),
                                                                      ExceptionMessageKeys.DEFAULT_ERROR_CODE,
                                                                      ErrorMessages.resolve(ExceptionMessageKeys.DEFAULT_ERROR_MESSAGE)));
  }

  /**
   * Builds a ResponseEntity containing the error response. Applies customizations to the error response before returning it.
   *
   * @param errorResponse The error response to build.
   * @return ResponseEntity containing the customized error response.
   */
  private ResponseEntity<Object> buildResponseError(ErrorResponse errorResponse) {
    log.debug("[ErrorExceptionHandler] - Building error response: {}", errorResponse);

    for (ErrorBuilderCustomizer customizer : errorBuilderCustomizers) {
      log.trace("[ErrorExceptionHandler] - Customizing error with: {}", customizer.getClass().getSimpleName());
      customizer.customize(errorResponse);
    }

    return ResponseEntity.status(errorResponse.getError().getStatus())
                         .cacheControl(CacheControlUtils.createCacheControl())
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(errorResponse);
  }
}
