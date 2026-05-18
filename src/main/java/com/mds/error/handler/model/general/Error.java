package com.mds.error.handler.model.general;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Core error model carrying all details of a single error occurrence.
 *
 * <p>Fields include a machine-readable {@code code}, a human-readable
 * {@code message}, an optional list of {@code messages}, an HTTP
 * {@code status}, a {@link Type} discriminator, a suggested
 * {@link Action}, an ISO-8601 {@code date}, and the originating
 * {@link Exception} (excluded from JSON serialisation).
 *
 * <p>Instances are typically created by
 * {@link com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper}
 * and wrapped inside an
 * {@link com.mds.error.handler.model.response.ErrorResponse}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_EMPTY)
public class Error implements Serializable {

  @Serial
  private static final long serialVersionUID = 4029570015643224130L;

  /**
   * The error code that identifies the type of error.
   */
  private String code;

  /**
   * The type of the error, represented by the {@link Type} enumerator.
   */
  private Type type;

  /**
   * The title or summary of the error.
   */
  private String title;

  /**
   * A detailed message describing the error.
   */
  private String message;

  /**
   * A list of additional messages related to the error.
   */
  private List<String> messages;

  /**
   * The HTTP status code associated with the error.
   */
  private Integer status;

  /**
   * The action to be taken, represented by the {@link Action} enumerator.
   */
  private Action action;

  /**
   * The date and time when the error occurred, in ISO-8601 format.
   */
  private String date;

  /**
   * The exception that caused the error. This field is ignored during JSON serialization.
   */
  @JsonIgnore
  private Exception exception;

  /**
   * Returns a formatted error message combining the status and exception message.
   * If the exception is null, it uses the main error message instead.
   *
   * @return a formatted error message.
   */
  @JsonIgnore
  public String getMessageError() {
    return "Status: "
          + status
          + " - Error: "
          + (exception != null ? exception.getMessage() : message);
  }

  /**
   * Constructs an error object with the specified parameters.
   *
   * @param exception the exception that caused the error
   * @param action    the action to be taken
   * @param type      the type of the error
   * @param status    the HTTP status code associated with the error
   * @param title     the title or summary of the error
   * @param code      the error code
   * @param message   additional messages describing the error
   */
  public Error(Exception exception, Action action, Type type, Integer status, String title, String code, String... message) {
    super();
    this.code = code;
    this.type = type;
    this.title = title;
    builderMessageValidator(message);
    this.status = status;
    this.action = action;
    this.exception = exception;
    this.date = Instant.now().toString();
  }

  /**
   * Constructs an error object with the specified parameters, excluding the title.
   *
   * @param exception the exception that caused the error
   * @param action    the action to be taken
   * @param type      the type of the error
   * @param status    the HTTP status code associated with the error
   * @param code      the error code
   * @param message   additional messages describing the error
   */
  public Error(Exception exception, Action action, Type type, Integer status, String code, String... message) {
    super();
    this.code = code;
    this.type = type;
    builderMessageValidator(message);
    this.status = status;
    this.action = action;
    this.exception = exception;
    this.date = Instant.now().toString();
  }

  /**
   * Validates and sets the error message(s) based on the provided array.
   * If the array contains more than one message, it sets the list of messages.
   * If the array contains exactly one message, it sets the main message.
   *
   * @param message an array of error messages
   */
  private void builderMessageValidator(String[] message) {
    if (message == null) return;
    if (message.length > 1) {
      messages = List.of(message);
    } else if (message.length == 1) {
      this.message = message[0];
    }
  }
}