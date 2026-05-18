package com.mds.error.handler;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot auto-configuration that bootstraps the MDS error-handling
 * framework by scanning all components under the
 * {@code com.mds.error.handler} package.
 *
 * <p>Registered automatically via {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@ComponentScan
public class ErrorHandlerAutoConfiguration {}
