package com.mds.error.handler.config;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Resolves internationalised error messages using Spring's {@link MessageSource}.
 *
 * <p>Provides both instance and static access so that messages can be resolved
 * from Spring-managed beans <em>and</em> from static contexts (e.g.
 * {@link com.mds.error.handler.model.response.ErrorResponse}).
 *
 * <p>The locale is determined by {@link LocaleContextHolder}, which is
 * automatically set by Spring MVC based on the {@code Accept-Language} header.
 *
 * @author MDS
 * @since 0.0.2-SNAPSHOT
 */
@Slf4j
@Component
public class ErrorMessages {

  private static MessageSource staticMessageSource;

  public ErrorMessages(MessageSource errorHandlerMessageSource) {
    ErrorMessages.staticMessageSource = errorHandlerMessageSource;
    log.debug("[ErrorMessages] - Initialized with MessageSource: {}", errorHandlerMessageSource.getClass().getSimpleName());
  }

  /**
   * Resolves a message key using the current request locale.
   *
   * @param key  the message key (e.g. {@code "error.default.title"})
   * @param args optional arguments for placeholder substitution
   * @return the resolved message, or the key itself if not found
   */
  public String get(String key, Object... args) {
    return resolve(key, args);
  }

  /**
   * Static accessor for contexts without Spring injection.
   *
   * <p>Falls back to returning the key itself if the {@link MessageSource}
   * has not been initialised yet (e.g. during static initialisation).
   *
   * @param key  the message key
   * @param args optional arguments for placeholder substitution
   * @return the resolved message, or the key itself as fallback
   */
  public static String resolve(String key, Object... args) {
    if (staticMessageSource == null) {
      log.warn("[ErrorMessages] - MessageSource not yet initialised; returning key '{}' as-is.", key);
      return key;
    }
    Locale locale = LocaleContextHolder.getLocale();
    return staticMessageSource.getMessage(key, args, key, locale);
  }
}
