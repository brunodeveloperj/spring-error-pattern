package com.mds.error.handler.exception.handler;

import static org.junit.jupiter.api.Assertions.*;

import com.mds.error.handler.config.ErrorHandlerJacksonConfig;
import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import com.mds.error.handler.exception.ProductException;
import com.mds.error.handler.exception.TechnicalException;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.exception.resolver.impl.ErrorResponseExceptionResolverImpl;
import com.mds.error.handler.model.response.ErrorResponse;
import com.mds.error.handler.utils.ErrorUtils;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ErrorExceptionHandlerTest {

  private ErrorExceptionHandler handler;

  @BeforeEach
  void setUp() {
    var mapper = new ErrorHandlerJacksonConfig().errorHandlerObjectMapper();
    ErrorUtils errorUtils = new ErrorUtils(mapper);

    List<ExceptionResolver<?>> resolvers = List.of(new ErrorResponseExceptionResolverImpl());

    handler = new ErrorExceptionHandler(errorUtils, resolvers, List.of());
  }

  @Test
  void shouldHandleGeneralException_withCorrectStatus() {
    ProductException ex = new ProductException("P001", "product error", "Title", Action.FUNCTIONAL, 422);

    ResponseEntity<Object> response = handler.handleGeneralException(ex);

    assertEquals(422, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertInstanceOf(ErrorResponse.class, response.getBody());

    ErrorResponse errorResponse = (ErrorResponse) response.getBody();
    assertEquals("P001", errorResponse.getError().getCode());
    assertEquals(Type.PRODUCT, errorResponse.getError().getType());
  }

  @Test
  void shouldHandleGeneralException_preservesAction() {
    TechnicalException ex = new TechnicalException("T001", "tech error", Action.RETRY_ON_STATE);

    ResponseEntity<Object> response = handler.handleGeneralException(ex);

    ErrorResponse errorResponse = (ErrorResponse) response.getBody();
    assertNotNull(errorResponse);
    assertEquals(Action.RETRY_ON_STATE, errorResponse.getError().getAction());
  }

  @Test
  void shouldHandleUnknownException_withGeneric500() {
    IllegalStateException ex = new IllegalStateException("unexpected");

    ResponseEntity<Object> response = handler.handleException(ex);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());

    ErrorResponse errorResponse = (ErrorResponse) response.getBody();
    assertNotNull(errorResponse);
    assertEquals(Type.TECHNICAL, errorResponse.getError().getType());
  }

  @Test
  void shouldApplyCustomizers() {
    var mapper = new ErrorHandlerJacksonConfig().errorHandlerObjectMapper();
    ErrorUtils errorUtils = new ErrorUtils(mapper);

    ErrorExceptionHandler customHandler = new ErrorExceptionHandler(
        errorUtils,
        List.of(),
        List.of(errorResponse -> errorResponse.getError().setTitle("Custom Title"))
    );

    ProductException ex = new ProductException("P001", "msg", Action.BACK_HOME);
    ResponseEntity<Object> response = customHandler.handleGeneralException(ex);

    ErrorResponse errorResponse = (ErrorResponse) response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Custom Title", errorResponse.getError().getTitle());
  }

  @Test
  void shouldSetCacheControlAndContentType() {
    ProductException ex = new ProductException("P001", "msg", Action.BACK_HOME);

    ResponseEntity<Object> response = handler.handleGeneralException(ex);

    assertNotNull(response.getHeaders().getCacheControl());
    assertEquals("application/json", response.getHeaders().getContentType().toString());
  }
}
