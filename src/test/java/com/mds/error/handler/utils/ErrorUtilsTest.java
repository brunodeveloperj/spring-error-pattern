package com.mds.error.handler.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.mds.error.handler.config.ErrorHandlerJacksonConfig;
import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import com.mds.error.handler.exception.AuthorizationException;
import com.mds.error.handler.exception.GeneralException;
import com.mds.error.handler.exception.ProductException;
import com.mds.error.handler.exception.TechnicalException;
import com.mds.error.handler.exception.TransactionalException;
import com.mds.error.handler.exception.ValidationException;
import com.mds.error.handler.model.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ErrorUtilsTest {

  private ErrorUtils errorUtils;

  @BeforeEach
  void setUp() {
    var mapper = new ErrorHandlerJacksonConfig().errorHandlerObjectMapper();
    errorUtils = new ErrorUtils(mapper);
  }

  @Test
  void generateError_shouldBuildErrorResponse() {
    ErrorResponse response = errorUtils.generateError(
        new RuntimeException("test"), Action.BACK_HOME, Type.TECHNICAL,
        500, "Error Title", "ERR001", "Error message");

    assertNotNull(response);
    assertEquals("ERR001", response.getError().getCode());
    assertEquals("Error message", response.getError().getMessage());
    assertEquals(Type.TECHNICAL, response.getError().getType());
    assertEquals(500, response.getError().getStatus());
  }

  @Test
  void throwError_shouldThrowProductException() {
    assertThrows(ProductException.class,
        () -> errorUtils.throwError("P001", "msg", Type.PRODUCT, Action.FUNCTIONAL));
  }

  @Test
  void throwError_shouldThrowAuthorizationException() {
    assertThrows(AuthorizationException.class,
        () -> errorUtils.throwError("S001", "msg", Type.SECURITY, Action.LOGOUT));
  }

  @Test
  void throwError_shouldThrowTechnicalException() {
    assertThrows(TechnicalException.class,
        () -> errorUtils.throwError("T001", "msg", Type.TECHNICAL, Action.RETRY_ON_STATE));
  }

  @Test
  void throwError_shouldThrowValidationException() {
    assertThrows(ValidationException.class,
        () -> errorUtils.throwError("V001", "msg", Type.VALIDATION, Action.VALIDATION_FEEDBACK));
  }

  @Test
  void throwError_shouldThrowTransactionalException() {
    assertThrows(TransactionalException.class,
        () -> errorUtils.throwError("TX001", "msg", Type.TRANSACTION, Action.BACK_HOME));
  }

  @Test
  void throwError_shouldThrowGeneralException_whenTypeIsNull() {
    assertThrows(GeneralException.class,
        () -> errorUtils.throwError("G001", "msg", (Type) null, Action.BACK_HOME));
  }

  @Test
  void getType_shouldMapExceptionClassToType() {
    assertEquals(Type.PRODUCT, errorUtils.getType(new ProductException("C", "m", Action.BACK_HOME)));
    assertEquals(Type.SECURITY, errorUtils.getType(new AuthorizationException("C", "m", Action.BACK_HOME)));
    assertEquals(Type.TRANSACTION, errorUtils.getType(new TransactionalException("C", "m", Action.BACK_HOME)));
    assertEquals(Type.VALIDATION, errorUtils.getType(new ValidationException("C", "m", Action.BACK_HOME)));
    assertEquals(Type.TECHNICAL, errorUtils.getType(new TechnicalException("C", "m", Action.BACK_HOME)));
  }

  @Test
  void getType_shouldDefaultToTechnical_forGeneralException() {
    assertEquals(Type.TECHNICAL, errorUtils.getType(new GeneralException("C", "m", Action.BACK_HOME)));
  }

  @Test
  void verifyError_shouldReturnNull_forNullInput() throws GeneralException {
    assertNull(errorUtils.verifyError(null, false));
  }

  @Test
  void verifyError_shouldReturnNull_forObjectWithoutError() throws GeneralException {
    assertNull(errorUtils.verifyError("not-json-object", false));
  }
}
