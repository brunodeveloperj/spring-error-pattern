package com.mds.error.handler.exception.resolver.impl;

import static com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper.createError;

import com.mds.error.handler.config.ErrorMessages;
import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import com.mds.error.handler.exception.keys.ExceptionMessageKeys;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.model.response.ErrorResponse;
import com.mds.error.handler.model.response.ErrorResponse.DeserializationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * {@link ExceptionResolver} for
 * {@link com.mds.error.handler.model.response.ErrorResponse.DeserializationException}.
 *
 * <p>Returns a {@code 500 INTERNAL_SERVER_ERROR} response with
 * {@link com.mds.error.handler.enumerator.Action#RETRY_ON_STATE RETRY_ON_STATE}
 * action, using the error code and message carried by the exception.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class ErrorResponseExceptionResolverImpl implements ExceptionResolver<DeserializationException> {

  /**
   * Resolves a DeserializationException into an ErrorResponse.
   *
   * @param error the DeserializationException to be resolved. It contains details about
   *              the error that occurred during the deserialization process.
   * @return an ErrorResponse object containing the error details, including the code,
   *         message, type, cause, title, action, and status.
   */
  @Override
  public ErrorResponse resolve(DeserializationException error) {
   return createError(error,
                      Action.RETRY_ON_STATE,
                      Type.TECHNICAL,
                      HttpStatus.INTERNAL_SERVER_ERROR.value(),
                      ErrorMessages.resolve(ExceptionMessageKeys.DEFAULT_ERROR_TITLE),
                      error.getCode(),
                      error.getMessage());
  }
}
