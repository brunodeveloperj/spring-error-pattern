package com.mds.error.handler.exception;

import com.mds.error.handler.enumerator.Action;
import java.io.Serial;

/**
 * Unchecked exception representing a <em>transactional / data-integrity</em> error.
 *
 * <p>Thrown when a database or messaging transaction fails
 * (e.g. constraint violation, rollback). Mapped to
 * {@link com.mds.error.handler.enumerator.Type#TRANSACTION} in the error
 * response.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see GeneralException
 */
public class TransactionalException extends GeneralException {

  @Serial private static final long serialVersionUID = 1L;

  public TransactionalException(String code, String message, Action action) {
    super(code, message, action);
  }

  public TransactionalException(
      String code, String message, String title, String detail, Action action) {
    super(code, message, title, detail, action);
  }

  public TransactionalException(
      String code, String message, String title, Action action, int httpStatusCode) {
    super(code, message, title, action, httpStatusCode);
  }

  public TransactionalException(
      String code,
      String message,
      String title,
      Action action,
      int httpStatusCode,
      boolean printLog) {
    super(code, message, title, action, httpStatusCode, printLog);
  }

  public TransactionalException(
      String code, String message, String title, String detail, Action action, Throwable error) {
    super(code, message, title, detail, action, error);
  }

  public TransactionalException(
      String code, String message, String detail, Action action, Throwable error) {
    super(code, message, detail, action, error);
  }

  public TransactionalException(String logMessage, Throwable error) {
    super(logMessage, error);
  }

  public TransactionalException(String code, String message, String title, Action action) {
    super(code, message, title, action);
  }
}
