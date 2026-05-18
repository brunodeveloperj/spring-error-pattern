package com.mds.error.handler.model.response;

import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DESERIALIZATION_EXCEPTION_CODE;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DESERIALIZATION_EXCEPTION_MESSAGE;

import com.mds.error.handler.config.ErrorMessages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import com.mds.error.handler.exception.base.BaseException;
import com.mds.error.handler.model.general.Error;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Standard error response DTO returned by the MDS error-handling framework.
 *
 * <p>Wraps a single {@link Error} object and provides convenience methods for
 * deserialising external error payloads (e.g. from downstream REST calls) and
 * for extracting a formatted error message.
 *
 * <p>Also declares a static inner {@link DeserializationException} thrown when
 * the incoming payload cannot be parsed.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

  /**
   * A static instance of {@link ObjectMapper} configured with the {@link JavaTimeModule}.
   * Used for JSON serialization and deserialization.
   */
  private static final ObjectMapper mapper;

  /**
   * The error object containing details about the error.
   */
  private Error error;

  // Static initializer aligned with ErrorHandlerJacksonConfig bean configuration.
  // Jackson 3 auto-detects modules (e.g. JavaTimeModule) from the classpath.
  static {
    mapper = JsonMapper.builder()
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
  }

  /**
   * Constructs an ErrorResponse by deserializing a JSON string payload.
   *
   * @param jsonPayload the JSON string to deserialize (may be {@code null}).
   * @throws DeserializationException if the payload cannot be parsed.
   */
  public ErrorResponse(String jsonPayload) {
    if (jsonPayload == null || jsonPayload.isBlank()) {
      return;
    }
    try {
      ErrorResponse content = mapper.readValue(jsonPayload, ErrorResponse.class);
      this.error = content.getError();
    } catch (JacksonException e) {
      throw new DeserializationException(DESERIALIZATION_EXCEPTION_CODE, ErrorMessages.resolve(DESERIALIZATION_EXCEPTION_MESSAGE), e);
    }
  }

  /**
   * Retrieves a formatted error message combining the error code and message.
   *
   * @return a string in the format "code - message".
   */
  @JsonIgnore
  public String getMessageError() {
    return error.getCode() + " - " + error.getMessage();
  }

  /**
   * Custom exception class for handling deserialization errors.
   * Extends {@link BaseException}.
   */
  public static class DeserializationException extends BaseException {
    /**
     * Constructs a DeserializationException with the specified code, message, and cause.
     *
     * @param code    the error code associated with the exception.
     * @param message the descriptive message of the exception.
     * @param cause   the original cause of the exception.
     */
    public DeserializationException(String code, String message, Throwable cause) {
      super(code, message, cause);
    }
  }

}
