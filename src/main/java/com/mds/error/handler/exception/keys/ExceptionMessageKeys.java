package com.mds.error.handler.exception.keys;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class holding constant error codes and i18n message keys used
 * across the MDS error-handling framework.
 *
 * <p>Error codes ({@code EHL_XXXX}) are locale-independent identifiers.
 * Message/title constants are <strong>resource-bundle keys</strong> resolved
 * at runtime via {@link com.mds.error.handler.config.ErrorMessages} and the
 * {@code Accept-Language} header.
 *
 * <p>Available bundles: {@code messages/error-handler-messages[_locale].properties}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessageKeys {

  /**
   * Default error code used when no specific code is provided.
   */
  public static final String DEFAULT_ERROR_CODE = "EHL_0000";

  /**
   * Message key for the default error title.
   * Resolved via {@code messages/error-handler-messages*.properties}.
   */
  public static final String DEFAULT_ERROR_TITLE = "error.default.title";

  /**
   * Message key for the default error message.
   * Resolved via {@code messages/error-handler-messages*.properties}.
   */
  public static final String DEFAULT_ERROR_MESSAGE = "error.default.message";

  /**
   * Error code for general exceptions related to invalid parameters.
   */
  public static final String CHECK_VALUE_GENERAL_EXCEPTION_CODE = "EHL_0001";

  /**
   * Message key for invalid-parameter errors.
   * Resolved via {@code messages/error-handler-messages*.properties}.
   */
  public static final String CHECK_VALUE_GENERAL_EXCEPTION_MESSAGE = "error.check-value.message";

  /**
   * Error code for deserialization exceptions.
   */
  public static final String DESERIALIZATION_EXCEPTION_CODE = "EHL_0002";

  /**
   * Message key for deserialization errors.
   * Resolved via {@code messages/error-handler-messages*.properties}.
   */
  public static final String DESERIALIZATION_EXCEPTION_MESSAGE = "error.deserialization.message";

  /**
   * Error code for method argument not valid exceptions.
   */
  public static final String METHOD_ARGUMENT_NOT_VALID_CODE = "EHL_0003";

  /**
   * Message key for method-argument-not-valid errors.
   * Resolved via {@code messages/error-handler-messages*.properties}.
   */
  public static final String METHOD_ARGUMENT_NOT_VALID_MESSAGE = "error.method-argument-not-valid.message";

}
