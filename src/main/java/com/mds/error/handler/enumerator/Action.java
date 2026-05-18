package com.mds.error.handler.enumerator;

/**
 * Defines the suggested client-side actions to be taken when an error occurs.
 *
 * <p>Each value represents a specific recovery or navigation action that the
 * consuming application should perform upon receiving an error response.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public enum Action {
  FUNCTIONAL,
  VALIDATION_FEEDBACK,
  RETRY_ON_STATE,
  BACK_HOME,
  WARNING,
  RESET_PASSWORD,
  LOGOUT
}
