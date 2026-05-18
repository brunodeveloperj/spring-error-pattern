package com.mds.error.handler.config;

import java.nio.charset.StandardCharsets;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Registers a {@link MessageSource} bean for the error handler module,
 * enabling internationalised error messages.
 *
 * <p>Messages are loaded from {@code messages/error-handler-messages*.properties}
 * resource bundles. The locale is resolved automatically by Spring MVC via the
 * {@code Accept-Language} header.
 *
 * <p>Consumers can override individual messages by placing a file with the same
 * basename on their classpath (standard Spring {@link ResourceBundleMessageSource}
 * behaviour).
 *
 * @author MDS
 * @since 0.0.2-SNAPSHOT
 */
@Configuration
public class ErrorHandlerMessageConfig {

  @Bean
  public MessageSource errorHandlerMessageSource() {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setBasename("messages/error-handler-messages");
    source.setDefaultEncoding(StandardCharsets.UTF_8.name());
    source.setUseCodeAsDefaultMessage(true);
    return source;
  }
}
