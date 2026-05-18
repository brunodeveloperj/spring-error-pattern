package com.mds.error.handler.interfaces;

/**
 * Functional interface for wrapping executable logic that may throw exceptions.
 *
 * <p>Used by {@link com.mds.error.handler.utils.ErrorUtils} validation methods
 * to execute business logic and automatically catch/convert exceptions into
 * standardized error responses.
 *
 * @param <T> the return type of the execution.
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public interface ExecutableErrorHandler<T> {
  T execute() throws Throwable;
}
