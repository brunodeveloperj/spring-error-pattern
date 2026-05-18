package com.mds.error.handler.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.mds.error.handler.config.ErrorHandlerJacksonConfig;
import com.mds.error.handler.exception.GeneralException;
import com.mds.error.handler.exception.ProductException;
import com.mds.error.handler.exception.TechnicalException;
import com.mds.error.handler.exception.TransactionalException;
import com.mds.error.handler.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThrowUtilsTest {

  private ThrowUtils throwUtils;

  @BeforeEach
  void setUp() {
    var mapper = new ErrorHandlerJacksonConfig().errorHandlerObjectMapper();
    ErrorUtils errorUtils = new ErrorUtils(mapper);
    throwUtils = new ThrowUtils(errorUtils);
  }

  @Test
  void throwErrorByProduct_shouldThrowProductException() {
    assertThrows(ProductException.class,
        () -> throwUtils.throwErrorByProduct("P001", "product error"));
  }

  @Test
  void throwErrorByValidation_shouldThrowValidationException() {
    assertThrows(ValidationException.class,
        () -> throwUtils.throwErrorByValidation("V001", "validation error"));
  }

  @Test
  void throwErrorByTechnical_shouldThrowTechnicalException() {
    assertThrows(TechnicalException.class,
        () -> throwUtils.throwErrorByTechnical("T001", "technical error"));
  }

  @Test
  void throwErrorByTransaction_shouldThrowTransactionalException() {
    assertThrows(TransactionalException.class,
        () -> throwUtils.throwErrorByTransaction("TX001", "transaction error"));
  }

  @Test
  void checkValueByProduct_shouldThrow_whenNull() {
    assertThrows(GeneralException.class,
        () -> throwUtils.checkValueByProduct(null, "P001", "value required"));
  }

  @Test
  void checkValueByProduct_shouldNotThrow_whenValuePresent() {
    assertDoesNotThrow(
        () -> throwUtils.checkValueByProduct("valid", "P001", "value required"));
  }

  @Test
  void checkValueByProduct_shouldThrow_whenBlankString() {
    assertThrows(GeneralException.class,
        () -> throwUtils.checkValueByProduct("  ", "P001", "blank not allowed"));
  }

  @Test
  void checkValueByProduct_shouldThrow_whenFalseBoolean() {
    assertThrows(GeneralException.class,
        () -> throwUtils.checkValueByProduct(Boolean.FALSE, "P001", "must be true"));
  }

  @Test
  void checkValueByProduct_shouldNotThrow_whenTrueBoolean() {
    assertDoesNotThrow(
        () -> throwUtils.checkValueByProduct(Boolean.TRUE, "P001", "must be true"));
  }

  @Test
  void checkValueGeneral_shouldThrow_whenNull() {
    assertThrows(GeneralException.class,
        () -> throwUtils.checkValueGeneral(null));
  }
}
