package com.mds.error.handler.exception;

import static org.junit.jupiter.api.Assertions.*;

import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.exception.base.BaseException;
import com.mds.error.handler.exception.keys.ErrorStatusKeys;
import org.junit.jupiter.api.Test;

class GeneralExceptionTest {

  @Test
  void shouldExtendBaseException() {
    GeneralException ex = new GeneralException("C001", "msg", Action.BACK_HOME);
    assertInstanceOf(BaseException.class, ex);
    assertInstanceOf(RuntimeException.class, ex);
  }

  @Test
  void shouldCarryCodeAndMessage() {
    GeneralException ex = new GeneralException("C001", "something failed", Action.RETRY_ON_STATE);
    assertEquals("C001", ex.getCode());
    assertEquals("something failed", ex.getMessage());
    assertEquals(Action.RETRY_ON_STATE, ex.getAction());
  }

  @Test
  void shouldDefaultToUnprocessableEntityStatus() {
    GeneralException ex = new GeneralException("C001", "msg", Action.BACK_HOME);
    assertEquals(ErrorStatusKeys.UNPROCESSABLE_ENTITY, ex.getHttpStatusCode());
  }

  @Test
  void shouldAcceptCustomHttpStatus() {
    GeneralException ex = new GeneralException("C001", "msg", "title", Action.WARNING, 503);
    assertEquals(503, ex.getHttpStatusCode());
    assertEquals("title", ex.getTitle());
  }

  @Test
  void shouldCarryTitleAndDetail() {
    GeneralException ex = new GeneralException("C001", "msg", "Title", "Detail text", Action.FUNCTIONAL);
    assertEquals("Title", ex.getTitle());
    assertEquals("Detail text", ex.getDetail());
  }

  @Test
  void shouldDefaultActionToBackHome_whenNull() {
    GeneralException ex = new GeneralException("C001", "msg", (Action) null);
    assertEquals(Action.BACK_HOME, ex.getAction());
  }

  @Test
  void shouldPreserveCause() {
    RuntimeException cause = new RuntimeException("root");
    GeneralException ex = new GeneralException("C001", "msg", "detail", Action.BACK_HOME, cause);
    assertSame(cause, ex.getCause());
    assertEquals("detail", ex.getDetail());
  }
}
