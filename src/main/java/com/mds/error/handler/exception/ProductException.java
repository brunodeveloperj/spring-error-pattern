package com.mds.error.handler.exception;

import com.mds.error.handler.enumerator.Action;
import java.io.Serial;

/**
 * Unchecked exception representing a <em>product / business-rule</em> error.
 *
 * <p>Thrown when a business invariant is violated (e.g. insufficient balance,
 * unsupported operation for the current product). Mapped to
 * {@link com.mds.error.handler.enumerator.Type#PRODUCT} in the error
 * response.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see GeneralException
 */
public class ProductException extends GeneralException {

  @Serial private static final long serialVersionUID = 1L;

  public ProductException(String code, String message, Action action) {
    super(code, message, action);
  }

  public ProductException(String code, String message, String title, String detail, Action action) {
    super(code, message, title, detail, action);
  }

  public ProductException(
      String code, String message, String title, Action action, int httpStatusCode) {
    super(code, message, title, action, httpStatusCode);
  }

  public ProductException(
      String code,
      String message,
      String title,
      Action action,
      int httpStatusCode,
      boolean printLog) {
    super(code, message, title, action, httpStatusCode, printLog);
  }

  public ProductException(
      String code, String message, String detail, Action action, Throwable error) {
    super(code, message, detail, action, error);
  }

  public ProductException(
      String code, String message, String title, String detail, Action action, Throwable error) {
    super(code, message, title, detail, action, error);
  }

  public ProductException(String logMessage, Throwable error) {
    super(logMessage, error);
  }

  public ProductException(String code, String message, String title, Action action) {
    super(code, message, title, action);
  }
}
