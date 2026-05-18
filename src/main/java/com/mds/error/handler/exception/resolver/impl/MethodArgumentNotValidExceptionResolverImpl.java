package com.mds.error.handler.exception.resolver.impl;

import static com.mds.error.handler.enumerator.Action.RETRY_ON_STATE;
import static com.mds.error.handler.enumerator.Type.VALIDATION;
import static com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper.createError;
import static com.mds.error.handler.exception.keys.ErrorStatusKeys.UNPROCESSABLE_ENTITY;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DEFAULT_ERROR_TITLE;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.METHOD_ARGUMENT_NOT_VALID_CODE;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.METHOD_ARGUMENT_NOT_VALID_MESSAGE;

import com.mds.error.handler.config.ErrorMessages;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.model.response.ErrorResponse;
import java.util.List;
import java.util.Map;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * {@link ExceptionResolver} for Spring's
 * {@link MethodArgumentNotValidException}.
 *
 * <p>Extracts field-level validation error messages from the binding result,
 * determines an appropriate error code, and returns a
 * {@code 422 UNPROCESSABLE_ENTITY} response with
 * {@link com.mds.error.handler.enumerator.Action#RETRY_ON_STATE RETRY_ON_STATE}
 * action. When a single validation message follows the {@code CODE - message}
 * convention, the code is split out automatically.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class MethodArgumentNotValidExceptionResolverImpl implements ExceptionResolver<MethodArgumentNotValidException> {

  /**
   * Resolves MethodArgumentNotValidException into a structured ErrorResponse.
   * Extracts validation error messages and determines appropriate error code and message.
   *
   * @param error the MethodArgumentNotValidException to resolve
   * @return a structured ErrorResponse representing the validation errors
   */
  @Override
  public ErrorResponse resolve(MethodArgumentNotValidException error) {
    final List<String> messages = extractErrorMessages(error);

    Map.Entry<String, String[]> codeAndMessage = determineCodeAndMessage(messages);
    String code = codeAndMessage.getKey();
    String[] message = codeAndMessage.getValue();

    return createError(error,
                       RETRY_ON_STATE,
                       VALIDATION,
                       UNPROCESSABLE_ENTITY,
                       ErrorMessages.resolve(DEFAULT_ERROR_TITLE),
                       code,
                       message);
  }

  /**
   * Extracts error messages from validation result.
   * @param error method argument validation exception
   * @return list of non-empty error messages
   */
  private List<String> extractErrorMessages(MethodArgumentNotValidException error) {
    return error.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(message -> message != null && !message.isBlank())
                .toList();
  }

  /**
   * Determines error code and message based on extracted messages.
   * If multiple messages exist, uses a generic code and aggregates messages.
   * If a single message exists, attempts to split into code and message.
   *
   * @param messages list of extracted error messages
   * @return entry containing determined error code and array of messages
   */
  private Map.Entry<String, String[]> determineCodeAndMessage(List<String> messages) {
    String code = METHOD_ARGUMENT_NOT_VALID_CODE;
    String[] message = new String[] {ErrorMessages.resolve(METHOD_ARGUMENT_NOT_VALID_MESSAGE)};

    if (!messages.isEmpty()) {
      if (messages.size() == 1) {
        String[] parts = messages.get(0).split("-");
        code = parts.length > 1 ? parts[0].trim() : code;
        message = new String[] {parts.length > 1 ? parts[1].trim() : messages.get(0)};
      } else {
        message = messages.toArray(String[]::new);
      }
    }

    return Map.entry(code, message);
  }

}

