package com.mds.error.handler.utils;

import static com.mds.error.handler.enumerator.Action.LOGOUT;
import static com.mds.error.handler.enumerator.Action.RETRY_ON_STATE;
import static com.mds.error.handler.enumerator.Action.VALIDATION_FEEDBACK;
import static com.mds.error.handler.enumerator.Type.PRODUCT;
import static com.mds.error.handler.enumerator.Type.SECURITY;
import static com.mds.error.handler.enumerator.Type.TECHNICAL;
import static com.mds.error.handler.enumerator.Type.TRANSACTION;
import static com.mds.error.handler.enumerator.Type.VALIDATION;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.CHECK_VALUE_GENERAL_EXCEPTION_CODE;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.CHECK_VALUE_GENERAL_EXCEPTION_MESSAGE;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DEFAULT_ERROR_TITLE;
import static com.mds.error.handler.exception.keys.ErrorStatusKeys.UNPROCESSABLE_ENTITY;

import com.mds.error.handler.config.ErrorMessages;
import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import com.mds.error.handler.exception.GeneralException;
import java.util.Collection;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Convenience component for throwing typed {@link GeneralException} variants
 * based on error category (product, validation, technical, security, transaction).
 *
 * <p>Provides {@code throwErrorBy*} methods to immediately throw and
 * {@code checkValueBy*} methods that throw only when the supplied value is
 * null, blank, empty, or {@code false}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see ErrorUtils
 */
@Component
@RequiredArgsConstructor
public class ThrowUtils {

  private final ErrorUtils errorUtils;

  public void checkValueGeneral(Object value) throws GeneralException {
    checkValueByTransaction(value, CHECK_VALUE_GENERAL_EXCEPTION_CODE, ErrorMessages.resolve(CHECK_VALUE_GENERAL_EXCEPTION_MESSAGE));
  }

  public void throwErrorByProduct(String code, String message) throws GeneralException {
    throwErrorByProduct(code, message, UNPROCESSABLE_ENTITY);
  }

  public void throwErrorByProduct(String code, String message, Action action) throws GeneralException {
    throwErrorByProduct(code, message, action, UNPROCESSABLE_ENTITY);
  }

  public void throwErrorByProduct(String code, String message, int httpStatus) throws GeneralException {
    throwErrorByProduct(code, message, RETRY_ON_STATE, httpStatus);
  }

  public void throwErrorByProduct(String code, String message, Action action, int httpStatus) throws GeneralException {
    buildError(code, message, DEFAULT_ERROR_TITLE, PRODUCT, action, httpStatus);
  }

  public void checkValueByProduct(Object value, String code, String message) throws GeneralException {
    checkValueByProduct(value, code, message, UNPROCESSABLE_ENTITY);
  }

  public void checkValueByProduct(Object value, String code, String message, Action action) throws GeneralException {
    checkValueByProduct(value, code, message, action, UNPROCESSABLE_ENTITY);
  }

  public void checkValueByProduct(Object value, String code, String message, int httpStatus) throws GeneralException {
    checkValueByProduct(value, code, message, RETRY_ON_STATE, httpStatus);
  }

  public void checkValueByProduct(Object value, String code, String message, Action action, int httpStatus) throws GeneralException {
    checkValue(value, code, message, PRODUCT, action, httpStatus);
  }

  public void throwErrorByValidation(String code, String message) throws GeneralException {
    throwErrorByValidation(code, message, UNPROCESSABLE_ENTITY);
  }

  public void throwErrorByValidation(String code, String message, int httpStatus) throws GeneralException {
    buildError(code, message, DEFAULT_ERROR_TITLE, VALIDATION, VALIDATION_FEEDBACK, httpStatus);
  }

  public void checkValueByValidation(Object value, String code, String message) throws GeneralException {
    checkValueByValidation(value, code, message, UNPROCESSABLE_ENTITY);
  }

  public void checkValueByValidation(Object value, String code, String message, int httpStatus) throws GeneralException {
    checkValue(value, code, message, VALIDATION, VALIDATION_FEEDBACK, httpStatus);
  }

  public void throwErrorByTechnical(String code, String message) throws GeneralException {
    throwErrorByTechnical(code, message, UNPROCESSABLE_ENTITY);
  }

  public void throwErrorByTechnical(String code, String message, int httpStatus) throws GeneralException {
    buildError(code, message, DEFAULT_ERROR_TITLE, TECHNICAL, RETRY_ON_STATE, httpStatus);
  }

  public void checkValueByTechnical(Object value, String code, String message) throws GeneralException {
    checkValueByTechnical(value, code, message, UNPROCESSABLE_ENTITY);
  }

  public void checkValueByTechnical(Object value, String code, String message, int httpStatus) throws GeneralException {
    checkValue(value, code, message, TECHNICAL, RETRY_ON_STATE, httpStatus);
  }

  public void throwErrorBySecurity(String code, String message) throws GeneralException {
    throwErrorBySecurity(code, message, UNPROCESSABLE_ENTITY);
  }

  public void throwErrorBySecurity(String code, String message, int httpStatus) throws GeneralException {
    buildError(code, message, DEFAULT_ERROR_TITLE, SECURITY, LOGOUT, httpStatus);
  }

  public void checkValueBySecurity(Object value, String code, String message) throws GeneralException {
    checkValueBySecurity(value, code, message, UNPROCESSABLE_ENTITY);
  }

  public void checkValueBySecurity(Object value, String code, String message, int httpStatus) throws GeneralException {
    checkValue(value, code, message, SECURITY, LOGOUT, httpStatus);
  }

  public void throwErrorByTransaction(String code, String message) throws GeneralException {
    throwErrorByTransaction(code, message, UNPROCESSABLE_ENTITY);
  }

  public void throwErrorByTransaction(String code, String message, Action action) throws GeneralException {
    throwErrorByTransaction(code, message, action, UNPROCESSABLE_ENTITY);
  }

  public void throwErrorByTransaction(String code, String message, int httpStatus) throws GeneralException {
    throwErrorByTransaction(code, message, RETRY_ON_STATE, httpStatus);
  }

  public void throwErrorByTransaction(String code, String message, Action action, int httpStatus) throws GeneralException {
    buildError(code, message, DEFAULT_ERROR_TITLE, TRANSACTION, action, httpStatus);
  }

  public void checkValueByTransaction(Object value, String code, String message) throws GeneralException {
    checkValueByTransaction(value, code, message, UNPROCESSABLE_ENTITY);
  }

  public void checkValueByTransaction(Object value, String code, String message, Action action) throws GeneralException {
    checkValueByTransaction(value, code, message, action, UNPROCESSABLE_ENTITY);
  }

  public void checkValueByTransaction(String code, String message, Action action, Object... value) throws GeneralException {
    for (Object o : value) {
      checkValueByTransaction(o, code, message, action, UNPROCESSABLE_ENTITY);
    }
  }

  public void checkValueByTransaction(Object value, String code, String message, int httpStatus) throws GeneralException {
    checkValueByTransaction(value, code, message, RETRY_ON_STATE, httpStatus);
  }

  public void checkValueByTransaction(Object value, String code, String message, Action action, int httpStatus) throws GeneralException {
    checkValue(value, code, message, TRANSACTION, action, httpStatus);
  }

  private void buildError(String code, String message, String titleKey, @NonNull Type type, Action action, int httpStatus) throws GeneralException {
    errorUtils.throwError(code, message, ErrorMessages.resolve(titleKey), type, action, httpStatus, false);
  }

  private void checkValue(Object value, String code, String message, Type type, Action action, int httpStatus) throws GeneralException {
    if (Objects.isNull(value)
        || (value instanceof String s && s.isBlank())
        || (value instanceof Collection<?> c && CollectionUtils.isEmpty(c))
        || (value instanceof Boolean b && !b)) {
      buildError(code, message, DEFAULT_ERROR_TITLE, type, action, httpStatus);
    }
  }
}
