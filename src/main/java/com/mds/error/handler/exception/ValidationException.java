package com.mds.error.handler.exception;

import com.mds.error.handler.enumerator.Action;
import java.io.Serial;

/**
 * Unchecked exception representing a <em>validation / input</em> error.
 *
 * <p>Thrown when user-supplied data fails validation rules (e.g. missing
 * required fields, format mismatch). Mapped to
 * {@link com.mds.error.handler.enumerator.Type#VALIDATION} in the error
 * response.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see GeneralException
 */
public class ValidationException extends GeneralException {

  @Serial private static final long serialVersionUID = 1L;

  public ValidationException(String code, String message, Action action) {
    super(code, message, action);
  }

  public ValidationException(
      String code, String message, String title, String detail, Action action) {
    super(code, message, title, detail, action);
  }

  public ValidationException(
      String code, String message, String title, Action action, int httpStatusCode) {
    super(code, message, title, action, httpStatusCode);
  }

  public ValidationException(
      String code,
      String message,
      String title,
      Action action,
      int httpStatusCode,
      boolean printLog) {
    super(code, message, title, action, httpStatusCode, printLog);
  }

  public ValidationException(
      String code, String message, String detail, Action action, Throwable error) {
    super(code, message, detail, action, error);
  }

  public ValidationException(
      String code, String message, String title, String detail, Action action, Throwable error) {
    super(code, message, title, detail, action, error);
  }

  public ValidationException(String logMessage, Throwable error) {
    super(logMessage, error);
  }

  public ValidationException(String code, String message, String title, Action action) {
    super(code, message, title, action);
  }
}
