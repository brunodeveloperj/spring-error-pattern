package com.mds.error.handler.exception.helper;

import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import com.mds.error.handler.model.general.Error;
import com.mds.error.handler.model.response.ErrorResponse;
import lombok.NoArgsConstructor;

/**
 * Static factory for creating {@link ErrorResponse} instances.
 *
 * <p>Provides two overloaded {@code createError} methods:
 * <ul>
 *   <li>One that accepts raw parameters (exception, action, type, status,
 *       title, code, messages) and builds a new {@link Error} internally.</li>
 *   <li>One that wraps an existing {@link Error} object directly.</li>
 * </ul>
 *
 * <p>Used primarily by
 * {@link com.mds.error.handler.exception.handler.ErrorExceptionHandler}
 * and {@link com.mds.error.handler.utils.ErrorUtils}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
public class ErrorExceptionHandlerHelper {

  /**
   * Creates an ErrorResponse object using the provided parameters.
   *
   * <p>This method constructs an {@link Error} object with the given details and wraps it in an
   * {@link ErrorResponse}. It is a utility method for simplifying the creation of error responses.
   *
   * @param code      The error code.
   * @param message   The error message.
   * @param type      The type of error (from the {@link Type} enum).
   * @param title     A brief title for the error.
   * @param action    Suggested action to resolve the error (from the {@link Action} enum).
   * @param exception The original exception that caused the error (if any).
   * @param status    The HTTP status code associated with the error.
   * @return An {@link ErrorResponse} object encapsulating the created error.
   */
  public static ErrorResponse createError(Exception exception,
                                          Action action,
                                          Type type,
                                          int status,
                                          String title,
                                          String code,
                                          String... message) {

    return new ErrorResponse(new Error(exception, action, type, status, title, code, message));
  }

  /**
   * Creates an ErrorResponse object using the provided Error instance.
   *
   * <p>This method wraps the given {@link Error} object into an {@link ErrorResponse}. It is a
   * utility method for simplifying the creation of error responses.
   *
   * @param error The {@link Error} object containing error details.
   * @return An {@link ErrorResponse} object encapsulating the provided error.
   */
  public static ErrorResponse createError(Error error) {

    return new ErrorResponse(error);
  }
}
