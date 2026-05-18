package com.mds.error.handler.config;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson configuration for the error handler module.
 *
 * <p>Provides a dedicated {@link ObjectMapper} bean used across the error handling
 * pipeline for JSON serialization and deserialization of error responses.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class ErrorHandlerJacksonConfig {

  /**
   * Creates a dedicated {@link ObjectMapper} bean for the error handler.
   *
   * @return a new {@link ObjectMapper} instance.
   */
  @Bean
  public ObjectMapper errorHandlerObjectMapper() {
    return JsonMapper.builder()
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
  }
}
