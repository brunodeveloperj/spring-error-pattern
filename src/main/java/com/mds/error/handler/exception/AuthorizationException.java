package com.mds.error.handler.exception;

import com.mds.error.handler.enumerator.Action;
import java.io.Serial;

/**
 * Unchecked exception representing an <em>authorization / authentication</em> error.
 *
 * <p>Thrown when an authentication or authorisation check fails
 * (e.g. invalid token, forbidden resource). Mapped to
 * {@link com.mds.error.handler.enumerator.Type#SECURITY} in the error
 * response.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see GeneralException
 */
public class AuthorizationException extends GeneralException {

  @Serial private static final long serialVersionUID = 1L;

  public AuthorizationException(String code, String message, Action action) {
    super(code, message, action);
  }

  public AuthorizationException(
      String code, String message, String title, String detail, Action action) {
    super(code, message, title, detail, action);
  }

  public AuthorizationException(
      String code, String message, String title, Action action, int httpStatusCode) {
    super(code, message, title, action, httpStatusCode);
  }

  public AuthorizationException(
      String code,
      String message,
      String title,
      Action action,
      int httpStatusCode,
      boolean printLog) {
    super(code, message, title, action, httpStatusCode, printLog);
  }

  public AuthorizationException(
      String code, String message, String detail, Action action, Throwable error) {
    super(code, message, detail, action, error);
  }

  public AuthorizationException(
      String code, String message, String title, String detail, Action action, Throwable error) {
    super(code, message, title, detail, action, error);
  }

  public AuthorizationException(String logMessage, Throwable error) {
    super(logMessage, error);
  }

  public AuthorizationException(String code, String message, String title, Action action) {
    super(code, message, title, action);
  }
}
