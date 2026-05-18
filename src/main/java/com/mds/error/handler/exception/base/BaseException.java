package com.mds.error.handler.exception.base;

import lombok.Getter;


/**
 * Abstract base for all <em>unchecked</em> custom exceptions in the MDS
 * framework. Extends {@link RuntimeException} and enriches it with an
 * {@code code} (application-level error identifier) and an HTTP
 * {@code status} code.
 *
 * <p>Subclasses are typically handled by
 * {@link com.mds.error.handler.exception.resolver.ExceptionResolver}
 * implementations which convert them into a standardised
 * {@link com.mds.error.handler.model.response.ErrorResponse}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public abstract class BaseException extends RuntimeException {

  /**
   * Error code associated with the exception.
   */
  private String code;
  private int status;

  /**
   * Default constructor for the BaseException class.
   * Calls the default constructor of the superclass {@link RuntimeException}.
   */
  public BaseException() {
    super();
  }

  /**
   * Constructor for the BaseException class with a message.
   * Calls the constructor of the superclass {@link RuntimeException} with the provided message.
   *
   * @param message the descriptive message of the exception
   */
  public BaseException(String message) {
    super(message);
  }

  /**
   * Constructor for the BaseException class with a message and status.
   * Calls the constructor of the superclass {@link RuntimeException} with the provided message.
   *
   * @param message the descriptive message of the exception
   * @param status the status code associated with the exception
   */
  public BaseException(String message, int status) {
    super(message);
    this.status = status;
  }

  /**
   * Constructor for the BaseException class with a cause.
   * Calls the constructor of the superclass {@link RuntimeException} with the provided cause.
   *
   * @param cause the original cause of the exception
   */
  public BaseException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructor for the BaseException class with a status and cause.
   * Calls the constructor of the superclass {@link RuntimeException} with the provided cause.
   *
   * @param status the status code associated with the exception
   * @param cause the original cause of the exception
   */
  public BaseException(int status, Throwable cause) {
    super(cause);
    this.status = status;
  }

  /**
   * Constructor that creates an exception with an error code and a message.
   *
   * @param code    the error code associated with the exception
   * @param message the descriptive message of the exception
   */
  public BaseException(String code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Constructor that creates an exception with an error code, a message and a status.
   *
   * @param code    the error code associated with the exception
   * @param message the descriptive message of the exception
   * @param status  the status code associated with the exception
   */
  public BaseException(String code, String message, int status) {
    super(message);
    this.code = code;
    this.status = status;
  }

  /**
   * Constructor that creates an exception with an error code, a message and a cause.
   *
   * @param code    the error code associated with the exception
   * @param message the descriptive message of the exception
   * @param cause   the original cause of the exception
   */
  public BaseException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Constructor that creates an exception with an error code, a message, a status and a cause.
   *
   * @param code    the error code associated with the exception
   * @param message the descriptive message of the exception
   * @param status  the status code associated with the exception
   * @param cause   the original cause of the exception
   */
  public BaseException(String code, String message, int status, Throwable cause) {
    super(message, cause);
    this.code = code;
    this.status = status;
  }
}
