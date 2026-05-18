package com.mds.error.handler.enumerator;

/**
 * Classifies the nature of an error within the error handling framework.
 *
 * <p>Used by the exception hierarchy and error response model to categorize
 * errors for proper routing, logging, and client-side handling.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public enum Type {
  TECHNICAL,
  SECURITY,
  VALIDATION,
  TRANSACTION,
  PRODUCT
}
