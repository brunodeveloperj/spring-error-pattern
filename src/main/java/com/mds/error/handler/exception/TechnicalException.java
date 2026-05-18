package com.mds.error.handler.exception;

import com.mds.error.handler.enumerator.Action;
import java.io.Serial;

/**
 * Unchecked exception representing a <em>technical / infrastructure</em> error.
 *
 * <p>Thrown when an unexpected infrastructure failure occurs (e.g. database
 * unreachable, external service timeout). Mapped to
 * {@link com.mds.error.handler.enumerator.Type#TECHNICAL} in the error
 * response.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see GeneralException
 */
public class TechnicalException extends GeneralException {

  @Serial private static final long serialVersionUID = 1L;

  public TechnicalException(String code, String message, Action action) {
    super(code, message, action);
  }

  public TechnicalException(
      String code, String message, String title, String detail, Action action) {
    super(code, message, title, detail, action);
  }

  public TechnicalException(
      String code, String message, String title, Action action, int httpStatusCode) {
    super(code, message, title, action, httpStatusCode);
  }

  public TechnicalException(
      String code,
      String message,
      String title,
      Action action,
      int httpStatusCode,
      boolean printLog) {
    super(code, message, title, action, httpStatusCode, printLog);
  }

  public TechnicalException(
      String code, String message, String title, String detail, Action action, Throwable error) {
    super(code, message, title, detail, action, error);
  }

  public TechnicalException(
      String code, String message, String detail, Action action, Throwable error) {
    super(code, message, detail, action, error);
  }

  public TechnicalException(String logMessage, Throwable error) {
    super(logMessage, error);
  }

  public TechnicalException(String code, String message, String title, Action action) {
    super(code, message, title, action);
  }
}
