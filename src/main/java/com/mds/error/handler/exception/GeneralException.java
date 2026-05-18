package com.mds.error.handler.exception;

import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.exception.base.BaseException;
import com.mds.error.handler.exception.keys.ErrorStatusKeys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Root exception for the MDS error handling framework.
 *
 * <p>All domain-specific exceptions ({@link com.mds.error.handler.exception.ProductException},
 * {@link com.mds.error.handler.exception.TechnicalException}, etc.) extend this class.
 * Carries an error code, human-readable message, title, detail, suggested action, and HTTP status.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see com.mds.error.handler.exception.handler.ErrorExceptionHandler
 */
@Slf4j
@Getter
public class GeneralException extends BaseException {

  private String detail;
  private int httpStatusCode = ErrorStatusKeys.UNPROCESSABLE_ENTITY;
  private Action action = Action.BACK_HOME;
  private String title = "";

  public GeneralException(String code, String message, Action action) {
    super(code, message);
    if (action != null) {
      this.action = action;
    }
    logInformation();
  }

  public GeneralException(String code, String message, String title, String detail, Action action) {
    super(code, message);
    this.detail = detail;
    this.title = title;
    if (action != null) {
      this.action = action;
    }
    logInformation();
  }

  public GeneralException(
      String code, String message, String title, Action action, int httpStatusCode) {
    super(code, message, httpStatusCode);
    this.title = title;
    this.httpStatusCode = httpStatusCode;
    if (action != null) {
      this.action = action;
    }
    logInformation();
  }

  public GeneralException(
      String code,
      String message,
      String title,
      Action action,
      int httpStatusCode,
      boolean printLog) {
    super(code, message, httpStatusCode);
    this.title = title;
    this.httpStatusCode = httpStatusCode;
    if (action != null) {
      this.action = action;
    }
    if (printLog) {
      logInformation();
    }
  }

  public GeneralException(
      String code, String message, String detail, Action action, Throwable error) {
    super(code, message, error);
    this.detail = detail;
    if (action != null) {
      this.action = action;
    }
    logInformation();
  }

  public GeneralException(
      String code, String message, String title, String detail, Action action, Throwable error) {
    super(code, message, error);
    this.detail = detail;
    this.title = title;
    if (action != null) {
      this.action = action;
    }
    logInformation();
  }

  public GeneralException(String detail, Throwable error) {
    super("", "", error);
    this.detail = detail;
    logInformation();
  }

  public GeneralException(String code, String message, String title, Action action) {
    super(code, message);
    this.title = title;
    if (action != null) {
      this.action = action;
    }
    logInformation();
  }

  public void logInformation() {
    String pattern = "[{}] - Exception info: code - {} ,\n message - {},\n title - {},\n detail - {}, action - {},\n httpStatusCode - {}";
    Object[] args = {getClass().getSimpleName(), getCode(), getMessage(), title, detail, action.name(), httpStatusCode};

    if (this instanceof ValidationException || this instanceof ProductException) {
      log.warn(pattern, args);
    } else {
      log.error(pattern, args);
    }
  }
}
