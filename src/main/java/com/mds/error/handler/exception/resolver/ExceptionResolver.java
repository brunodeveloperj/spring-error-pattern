package com.mds.error.handler.exception.resolver;

import com.mds.error.handler.model.response.ErrorResponse;
import java.lang.reflect.ParameterizedType;

/**
 * Strategy interface for converting a specific {@link Exception} subtype
 * into a standardised {@link ErrorResponse}.
 *
 * <p>Implementations are auto-detected by
 * {@link com.mds.error.handler.exception.handler.ErrorExceptionHandler}
 * and queried via {@link #supports(Exception)} to decide which resolver
 * handles a given exception. The resolver then
 * {@linkplain #convertToTypedException(Exception) casts} and
 * {@linkplain #resolve(Exception) resolves} it into an {@link ErrorResponse}.
 *
 * @param <T> the concrete exception type this resolver handles
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public interface ExceptionResolver<T extends Exception> {

  /**
   * Retrieves the type parameter of the implementing class.
   *
   * <p>This method uses reflection to obtain the generic type parameter of the interface
   * implemented by the current class. It assumes that the implementing class directly specifies the
   * generic type parameter in its interface declaration.
   *
   * @return The {@link Class} object representing the type parameter of the implementing class.
   * @throws ClassCastException If the generic interface type cannot be cast to {@link Class<T>}.
   */
  @SuppressWarnings("unchecked")
  default Class<T> getType() {
    Class<?> clazz = this.getClass();
    while (clazz != null) {
      for (java.lang.reflect.Type iface : clazz.getGenericInterfaces()) {
        if (iface instanceof ParameterizedType pt
            && ExceptionResolver.class.isAssignableFrom((Class<?>) pt.getRawType())) {
          return (Class<T>) pt.getActualTypeArguments()[0];
        }
      }
      clazz = clazz.getSuperclass();
    }
    throw new IllegalStateException(
        "Could not resolve type parameter for " + this.getClass().getName());
  }

  /**
   * Converts an exception into an object of the generic type parameter.
   *
   * <p>This method casts the provided {@link Exception} to the generic type {@code T}. It assumes
   * that the exception can be safely cast to the specified type.
   *
   * @param error The exception to be converted.
   * @return The exception cast to the generic type {@code T}.
   * @throws ClassCastException If the exception cannot be cast to the generic type {@code T}.
   */
  default T convertToTypedException(Exception error) {
    if (error == null) {
      throw new IllegalArgumentException("Error cannot be null");
    }
    Class<T> type = getType();
    if (type.isInstance(error)) {
      return type.cast(error);
    }
    throw new ClassCastException("Cannot cast exception to " + type.getName());
  }

  /**
   * Determines whether the resolver supports the given exception.
   *
   * @param error The exception to check.
   * @return {@code true} if the resolver can handle the exception, {@code false} otherwise.
   */
  default boolean supports(Exception error) {
    return error != null && getType().isInstance(error);
  }

  /**
   * Resolves the given exception into an {@link ErrorResponse}.
   *
   * @param error The exception to resolve.
   * @return An {@link ErrorResponse} representing the resolved exception.
   */
  ErrorResponse resolve(T error);
}
