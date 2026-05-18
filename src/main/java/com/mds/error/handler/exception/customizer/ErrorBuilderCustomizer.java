package com.mds.error.handler.exception.customizer;

import com.mds.error.handler.model.response.ErrorResponse;

/**
 * Functional interface for customising {@link ErrorResponse} objects before
 * they are returned to the client.
 *
 * <p>Register one or more implementations as Spring {@code @Component} beans
 * and they will be invoked by
 * {@link com.mds.error.handler.exception.handler.ErrorExceptionHandler}
 * after the error is built. Typical use cases include appending a
 * correlation/trace ID, masking sensitive fields, or enriching the response
 * with contextual metadata.
 *
 * <p>Example usage:
 *
 * <pre>
 * public class CustomErrorBuilder implements ErrorBuilderCustomizer {
 *     &#64;Override
 *     public void customize(ErrorResponse errorResponse) {
 *         errorResponse.setDetail("Custom details");
 *     }
 * }
 * </pre>
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@FunctionalInterface
public interface ErrorBuilderCustomizer {

  /**
   * Method to customize the error response object.
   *
   * @param errorResponse The `ErrorResponse` object to be modified.
   */
  void customize(ErrorResponse errorResponse);
}
