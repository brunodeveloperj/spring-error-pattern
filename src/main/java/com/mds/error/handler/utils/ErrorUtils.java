package com.mds.error.handler.utils;

import static com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper.createError;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.mds.error.handler.config.ErrorMessages;
import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import com.mds.error.handler.exception.GeneralException;
import com.mds.error.handler.exception.ProductException;
import com.mds.error.handler.exception.AuthorizationException;
import com.mds.error.handler.exception.TechnicalException;
import com.mds.error.handler.exception.TransactionalException;
import com.mds.error.handler.exception.ValidationException;
import com.mds.error.handler.exception.keys.ErrorStatusKeys;
import com.mds.error.handler.exception.keys.ExceptionMessageKeys;
import com.mds.error.handler.interfaces.ExecutableErrorHandler;
import com.mds.error.handler.model.general.Error;
import com.mds.error.handler.model.response.ErrorResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Spring-managed utility providing helper methods for the MDS error-handling
 * framework.
 *
 * <p>Key capabilities:
 * <ul>
 *   <li>{@code generateError} – builds an {@link ErrorResponse} from raw parameters</li>
 *   <li>{@code throwError} – creates and throws the appropriate typed
 *       {@link GeneralException} subclass based on a {@link Type} discriminator</li>
 *   <li>{@code verifyError} – inspects an arbitrary object for an embedded error
 *       payload and optionally re-throws it</li>
 *   <li>{@code validateGeneral*} – execute-and-catch wrappers that translate
 *       any {@link Throwable} into a typed exception</li>
 *   <li>{@code converterExceptionContent} / {@code converterHttpClientErrorException}
 *       – deserialise external error payloads into the framework's exception hierarchy</li>
 * </ul>
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
public class ErrorUtils {

  private final ObjectMapper objectMapper;

  public ErrorUtils(@Qualifier("errorHandlerObjectMapper") ObjectMapper errorHandlerObjectMapper) {
    this.objectMapper = errorHandlerObjectMapper;
  }

  public <E extends ErrorResponse> void generateError(E obj,
                                                      Exception exception,
                                                      Action action,
                                                      Type type,
                                                      Integer status,
                                                      String title,
                                                      String code,
                                                      String... message) {

    ErrorResponse generatedError = generateError(exception, action, type, status, title, code, message);
    obj.setError(generatedError.getError());
  }

  public <E extends ErrorResponse> void generateError(E obj,
                                                      Exception exception,
                                                      Action action,
                                                      Type type,
                                                      Integer status,
                                                      String code,
                                                      String... message) {

    ErrorResponse generatedError = generateError(exception, action, type, status, null, code, message);
    obj.setError(generatedError.getError());
  }

  public ErrorResponse generateError(Exception exception,
                                     Action action,
                                     Type type,
                                     Integer status,
                                     String title,
                                     String code,
                                     String... message) {

    return createError(exception, action, type, status, title, code, message);
  }

  public void throwError(String code, String message, Type type, Action action) throws GeneralException {
    throw newException(type, code, message, action);
  }

  public void throwError(String code, String message, String title, String detail, Type type, Action action) throws GeneralException {
    throw newExceptionWithTitleDetail(type, code, message, title, detail, action);
  }

  public void throwError(String code, String message, String title, Type type, Action action) throws GeneralException {
    throw newExceptionWithTitle(type, code, message, title, action);
  }

  public void throwError(String code, String message, String title, Type type, Action action, int httpStatusCode) throws GeneralException {
    throw newExceptionWithStatus(type, code, message, title, action, httpStatusCode);
  }

  public void throwError(String code, String message, String title, Type type, Action action, int httpStatusCode, boolean printLog) throws GeneralException {
    throw newExceptionWithStatusLog(type, code, message, title, action, httpStatusCode, printLog);
  }

  public void throwError(String code, String message, String detail, Type type, Action action, Throwable error) throws GeneralException {
    throw newExceptionWithDetailCause(type, code, message, detail, action, error);
  }

  public void throwError(String code, String message, String title, String detail, Type type, Action action, Throwable error) throws GeneralException {
    throw newExceptionWithTitleDetailCause(type, code, message, title, detail, action, error);
  }

  public void throwError(String logMessage, Throwable error, Type type) throws GeneralException {
    throw newExceptionWithCause(type, logMessage, error);
  }

  public void throwError(GeneralException generalException) throws GeneralException {
    Type type = getType(generalException);
    throw newExceptionWithStatus(type, generalException.getCode(), generalException.getMessage(),
        generalException.getTitle(), generalException.getAction(), generalException.getHttpStatusCode());
  }

  /**
   * Checks if the obj has any errors. <br> It is used after the return of a REST call that is using the same error pattern. <br>
   * <br>
   * throwError <code>true</code> :<br> When the obj has an error it will be thrown. <br>
   * <br>
   * throwError <code>false</code> :<br> When the obj has an error the exception will be returned.<br>
   * <br>
   * If the obj has no error or is null, it will be returned. <code>null</code>.<br>
   */
  @SuppressWarnings("unchecked")
  public GeneralException verifyError(Object obj, boolean throwError) throws GeneralException {
    if (obj == null) {
      return null;
    }

    try {
      String objAsString = objectMapper.writeValueAsString(obj);
      Map<String, Object> map = objectMapper.readValue(objAsString, new TypeReference<>() {
      });
      Map<String, Object> error = (Map<String, Object>) map.get("error");
      if (error == null) {
        return null;
      }
      String code = (String) error.get("code");
      String message = (String) error.get("message");
      Type type = Type.valueOf((String) error.get("type"));
      String title = (String) error.get("title");
      String detail = (String) error.get("detail");
      Action action = Action.valueOf((String) error.get("action"));

      if ((code != null && !code.isBlank()) && (message != null && !message.isBlank())) {
        GeneralException exception = returnException(code, message, type, title, detail, action);
        if (throwError) {
          throw exception;
        }
        return exception;
      }
    } catch (JacksonException e) {
      log.info("[ErrorUtil] - Error while verifying with verifyError", e);
    }
    return null;
  }

  public Type getType(GeneralException exception) {
    if (exception instanceof AuthorizationException) return Type.SECURITY;
    if (exception instanceof TransactionalException) return Type.TRANSACTION;
    if (exception instanceof ValidationException) return Type.VALIDATION;
    if (exception instanceof ProductException) return Type.PRODUCT;
    return Type.TECHNICAL;
  }

  public Object validateGeneralTransaction(ExecutableErrorHandler<?> exe, String code, String message, Action action) throws GeneralException {
    return validate(exe, code, message, ErrorMessages.resolve(ExceptionMessageKeys.DEFAULT_ERROR_TITLE), Type.TRANSACTION, true, action, ErrorStatusKeys.UNPROCESSABLE_ENTITY);
  }

  public Object validateGeneralSecurity(ExecutableErrorHandler<?> exe, String code, String message, Action action, int statusCode) throws GeneralException {
    return validate(exe, code, message, ErrorMessages.resolve(ExceptionMessageKeys.DEFAULT_ERROR_TITLE), Type.SECURITY, true, action, statusCode);
  }

  public Object validateGeneralProduct(ExecutableErrorHandler<?> exe, String code, String message, Action action, int statusCode) throws GeneralException {
    return validate(exe, code, message, ErrorMessages.resolve(ExceptionMessageKeys.DEFAULT_ERROR_TITLE), Type.PRODUCT, true, action, statusCode);
  }

  public Object validateGeneralTechnical(ExecutableErrorHandler<?> exe, String code, String message, Action action, int statusCode) throws GeneralException {
    return validate(exe, code, message, ErrorMessages.resolve(ExceptionMessageKeys.DEFAULT_ERROR_TITLE), Type.TECHNICAL, true, action, statusCode);
  }

  /**
   *
   *
   * <pre>
   *   This method is responsible for converting the contents of the exception into an error class.
   *   Example:
   *    - exceptionContent parameter: { "error": { "code": "FVEEHL_0000", "type": "PRODUCT", "title": "An error occurred!", "message": "Testing", "status": 422, "action": "RETRY_ON_STATE", "date": "2023-05-19T17:39:44.144300700Z"}}
   * </pre>
   */
  public Error converterExceptionContent(Object exceptionContent) {
    final var errorResponse = new ErrorResponse(toJsonString(exceptionContent));
    return errorResponse.getError();
  }

  /**
   *
   *
   * <pre>
   *   This method is responsible for converting the contents of the exception into an GeneralException class.
   *   Example:
   *   	- exceptionContent parameter: { "error": { "code": "FVEEHL_0000", "type": "PRODUCT", "title": "An error occurred!", "message": "Testing", "status": 422, "action": "RETRY_ON_STATE", "date": "2023-05-19T17:39:44.144300700Z"}}
   *   	- throwError parameter:
   *   		. false -> does not throw exception
   *   		. true -> throw exception
   * </pre>
   */
  public GeneralException converterExceptionContent(Object exceptionContent, boolean throwError) throws GeneralException {
    final var errorResponse = new ErrorResponse(toJsonString(exceptionContent));
    return verifyError(errorResponse, throwError);
  }

  private String toJsonString(Object payload) {
    if (payload == null) return null;
    if (payload instanceof String s) return s;
    log.warn("[ErrorUtil] - converterExceptionContent received non-String payload of type {}; attempting serialization.",
        payload.getClass().getSimpleName());
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JacksonException e) {
      log.error("[ErrorUtil] - Failed to serialize payload to JSON.", e);
      return null;
    }
  }

  /**
   * Converts an HTTP Client Error exception into a GeneralException, preserving the original exception details.
   *
   * @param httpClientErrorException The HttpClientErrorException to convert.
   * @return The converted GeneralException.
   * @throws GeneralException If the exception body is empty or during conversion.
   */
  public GeneralException converterHttpClientErrorException(HttpClientErrorException httpClientErrorException) throws GeneralException {
    String responseBody = httpClientErrorException.getResponseBodyAsString();
    if (responseBody.isEmpty()) {
      log.warn("[ErrorUtil] - Body of the exception is empty during conversion.");
      throw new TechnicalException(
          ExceptionMessageKeys.DEFAULT_ERROR_CODE,
          "Body of the exception is empty during conversion.",
          Action.RETRY_ON_STATE);
    }
    return converterExceptionContent(responseBody, true);
  }

  public Object validateGeneralValidation(ExecutableErrorHandler<?> exe, String code, String message, Action action, int statusCode) throws GeneralException {
    return validate(exe, code, message, ErrorMessages.resolve(ExceptionMessageKeys.DEFAULT_ERROR_TITLE), Type.VALIDATION, true, action, statusCode);
  }

  private Object validate(ExecutableErrorHandler<?> exe, String code, String message, String title, Type type, boolean throwError, Action action, int statusCode) throws GeneralException {
    try {
      log.info("[ErrorUtil] - (validate): Executing with throwError={}, action={}, type={}", throwError, action.name(), type.name());
      return exe.execute();
    } catch (Throwable t) {
      log.error("[ErrorUtil] - (validate): Error during execution.", t);
      if (throwError) {
        throwError(code, message, title, type, action, statusCode);
      }
    }
    return null;
  }

  private GeneralException returnException(String code, String message, Type type, String title, String detail, Action action) {
    return newExceptionWithTitleDetail(type, code, message, title, detail, action);
  }

  // ── Typed exception dispatch methods ───────────────────────────────────
  // Each method encapsulates a single switch for one constructor signature,
  // eliminating the repeated switch blocks that were in every throwError overload.

  private GeneralException newException(Type type, String code, String message, Action action) {
    if (type == null) return new GeneralException(code, message, action);
    return switch (type) {
      case SECURITY -> new AuthorizationException(code, message, action);
      case TECHNICAL -> new TechnicalException(code, message, action);
      case TRANSACTION -> new TransactionalException(code, message, action);
      case VALIDATION -> new ValidationException(code, message, action);
      case PRODUCT -> new ProductException(code, message, action);
    };
  }

  private GeneralException newExceptionWithTitle(Type type, String code, String message, String title, Action action) {
    if (type == null) return new GeneralException(code, message, title, action);
    return switch (type) {
      case SECURITY -> new AuthorizationException(code, message, title, action);
      case TECHNICAL -> new TechnicalException(code, message, title, action);
      case TRANSACTION -> new TransactionalException(code, message, title, action);
      case VALIDATION -> new ValidationException(code, message, title, action);
      case PRODUCT -> new ProductException(code, message, title, action);
    };
  }

  private GeneralException newExceptionWithTitleDetail(Type type, String code, String message, String title, String detail, Action action) {
    if (type == null) return new GeneralException(code, message, title, detail, action);
    return switch (type) {
      case SECURITY -> new AuthorizationException(code, message, title, detail, action);
      case TECHNICAL -> new TechnicalException(code, message, title, detail, action);
      case TRANSACTION -> new TransactionalException(code, message, title, detail, action);
      case VALIDATION -> new ValidationException(code, message, title, detail, action);
      case PRODUCT -> new ProductException(code, message, title, detail, action);
    };
  }

  private GeneralException newExceptionWithStatus(Type type, String code, String message, String title, Action action, int httpStatusCode) {
    if (type == null) return new GeneralException(code, message, title, action, httpStatusCode);
    return switch (type) {
      case SECURITY -> new AuthorizationException(code, message, title, action, httpStatusCode);
      case TECHNICAL -> new TechnicalException(code, message, title, action, httpStatusCode);
      case TRANSACTION -> new TransactionalException(code, message, title, action, httpStatusCode);
      case VALIDATION -> new ValidationException(code, message, title, action, httpStatusCode);
      case PRODUCT -> new ProductException(code, message, title, action, httpStatusCode);
    };
  }

  private GeneralException newExceptionWithStatusLog(Type type, String code, String message, String title, Action action, int httpStatusCode, boolean printLog) {
    if (type == null) return new GeneralException(code, message, title, action, httpStatusCode, printLog);
    return switch (type) {
      case SECURITY -> new AuthorizationException(code, message, title, action, httpStatusCode, printLog);
      case TECHNICAL -> new TechnicalException(code, message, title, action, httpStatusCode, printLog);
      case TRANSACTION -> new TransactionalException(code, message, title, action, httpStatusCode, printLog);
      case VALIDATION -> new ValidationException(code, message, title, action, httpStatusCode, printLog);
      case PRODUCT -> new ProductException(code, message, title, action, httpStatusCode, printLog);
    };
  }

  private GeneralException newExceptionWithDetailCause(Type type, String code, String message, String detail, Action action, Throwable cause) {
    if (type == null) return new GeneralException(code, message, detail, action, cause);
    return switch (type) {
      case SECURITY -> new AuthorizationException(code, message, detail, action, cause);
      case TECHNICAL -> new TechnicalException(code, message, detail, action, cause);
      case TRANSACTION -> new TransactionalException(code, message, detail, action, cause);
      case VALIDATION -> new ValidationException(code, message, detail, action, cause);
      case PRODUCT -> new ProductException(code, message, detail, action, cause);
    };
  }

  private GeneralException newExceptionWithTitleDetailCause(Type type, String code, String message, String title, String detail, Action action, Throwable cause) {
    if (type == null) return new GeneralException(code, message, title, detail, action, cause);
    return switch (type) {
      case SECURITY -> new AuthorizationException(code, message, title, detail, action, cause);
      case TECHNICAL -> new TechnicalException(code, message, title, detail, action, cause);
      case TRANSACTION -> new TransactionalException(code, message, title, detail, action, cause);
      case VALIDATION -> new ValidationException(code, message, title, detail, action, cause);
      case PRODUCT -> new ProductException(code, message, title, detail, action, cause);
    };
  }

  private GeneralException newExceptionWithCause(Type type, String logMessage, Throwable cause) {
    if (type == null) return new GeneralException(logMessage, cause);
    return switch (type) {
      case SECURITY -> new AuthorizationException(logMessage, cause);
      case TECHNICAL -> new TechnicalException(logMessage, cause);
      case TRANSACTION -> new TransactionalException(logMessage, cause);
      case VALIDATION -> new ValidationException(logMessage, cause);
      case PRODUCT -> new ProductException(logMessage, cause);
    };
  }

}
