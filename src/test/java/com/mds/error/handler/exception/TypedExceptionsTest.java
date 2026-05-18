package com.mds.error.handler.exception;

import static org.junit.jupiter.api.Assertions.*;

import com.mds.error.handler.enumerator.Action;
import org.junit.jupiter.api.Test;

class TypedExceptionsTest {

  @Test
  void productExceptionShouldExtendGeneralException() {
    ProductException ex = new ProductException("P001", "product error", Action.FUNCTIONAL);
    assertInstanceOf(GeneralException.class, ex);
    assertEquals("P001", ex.getCode());
  }

  @Test
  void authorizationExceptionShouldExtendGeneralException() {
    AuthorizationException ex = new AuthorizationException("S001", "auth error", Action.LOGOUT);
    assertInstanceOf(GeneralException.class, ex);
    assertEquals(Action.LOGOUT, ex.getAction());
  }

  @Test
  void technicalExceptionShouldExtendGeneralException() {
    TechnicalException ex = new TechnicalException("T001", "tech error", Action.RETRY_ON_STATE);
    assertInstanceOf(GeneralException.class, ex);
    assertEquals("tech error", ex.getMessage());
  }

  @Test
  void validationExceptionShouldExtendGeneralException() {
    ValidationException ex = new ValidationException("V001", "validation error", Action.VALIDATION_FEEDBACK);
    assertInstanceOf(GeneralException.class, ex);
    assertEquals(Action.VALIDATION_FEEDBACK, ex.getAction());
  }

  @Test
  void transactionalExceptionShouldExtendGeneralException() {
    TransactionalException ex = new TransactionalException("TX001", "tx error", Action.BACK_HOME);
    assertInstanceOf(GeneralException.class, ex);
    assertEquals("TX001", ex.getCode());
  }

  @Test
  void allTypedExceptionsShouldBeRuntimeExceptions() {
    assertInstanceOf(RuntimeException.class, new ProductException("C", "m", Action.BACK_HOME));
    assertInstanceOf(RuntimeException.class, new AuthorizationException("C", "m", Action.BACK_HOME));
    assertInstanceOf(RuntimeException.class, new TechnicalException("C", "m", Action.BACK_HOME));
    assertInstanceOf(RuntimeException.class, new ValidationException("C", "m", Action.BACK_HOME));
    assertInstanceOf(RuntimeException.class, new TransactionalException("C", "m", Action.BACK_HOME));
  }
}
