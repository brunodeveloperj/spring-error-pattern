package com.mds.error.handler.model;

import static org.junit.jupiter.api.Assertions.*;

import com.mds.error.handler.enumerator.Action;
import com.mds.error.handler.enumerator.Type;
import com.mds.error.handler.model.general.Error;
import com.mds.error.handler.model.response.ErrorResponse;
import org.junit.jupiter.api.Test;

class ErrorModelTest {

  @Test
  void error_shouldSetSingleMessage() {
    Error error = Error.builder()
        .code("ERR001").message("Single message").type(Type.TECHNICAL)
        .status(500).action(Action.BACK_HOME).build();

    assertEquals("Single message", error.getMessage());
    assertNull(error.getMessages());
  }

  @Test
  void error_shouldSetMultipleMessages() {
    Error error = Error.builder()
        .code("ERR001").messages(java.util.List.of("msg1", "msg2"))
        .type(Type.TECHNICAL).status(500).action(Action.BACK_HOME).build();

    assertNull(error.getMessage());
    assertNotNull(error.getMessages());
    assertEquals(2, error.getMessages().size());
    assertEquals("msg1", error.getMessages().get(0));
    assertEquals("msg2", error.getMessages().get(1));
  }

  @Test
  void error_shouldSetDateViaConstructor() {
    // Use the non-ambiguous constructor: (Exception, Action, Type, Integer, String, String...)
    // where title is omitted
    RuntimeException ex = new RuntimeException("test");
    String[] msgs = {"msg"};
    Error error = new Error(ex, Action.BACK_HOME, Type.TECHNICAL, 500, "ERR001", msgs);

    assertNotNull(error.getDate());
  }

  @Test
  void error_getMessageError_shouldFormatCorrectly() {
    RuntimeException ex = new RuntimeException("test exception");
    Error error = Error.builder()
        .code("ERR001").message("msg").type(Type.TECHNICAL)
        .status(500).action(Action.BACK_HOME).exception(ex).build();

    String messageError = error.getMessageError();
    assertEquals("Status: 500 - Error: test exception", messageError);
  }

  @Test
  void error_getMessageError_shouldFallbackToMessage_whenNoException() {
    Error error = Error.builder()
        .code("ERR001")
        .status(500)
        .message("fallback msg")
        .build();

    String messageError = error.getMessageError();
    assertEquals("Status: 500 - Error: fallback msg", messageError);
  }

  @Test
  void errorResponse_shouldWrapError() {
    Error error = Error.builder()
        .code("ERR001").message("msg").type(Type.TECHNICAL)
        .status(500).action(Action.BACK_HOME).build();
    ErrorResponse response = new ErrorResponse(error);

    assertNotNull(response.getError());
    assertEquals("ERR001", response.getError().getCode());
  }

  @Test
  void errorResponse_getMessageError_shouldFormatCodeAndMessage() {
    Error error = Error.builder().code("ERR001").message("something failed").build();
    ErrorResponse response = new ErrorResponse(error);

    assertEquals("ERR001 - something failed", response.getMessageError());
  }

  @Test
  void errorResponse_deserializationConstructor_shouldReturnNullError_forNullPayload() {
    ErrorResponse response = new ErrorResponse((String) null);
    assertNull(response.getError());
  }

  @Test
  void errorResponse_deserializationConstructor_shouldThrowDeserializationException_forInvalidJson() {
    assertThrows(ErrorResponse.DeserializationException.class,
        () -> new ErrorResponse("invalid json"));
  }

  @Test
  void errorResponse_deserializationConstructor_shouldDeserializeValidJson() {
    String json = "{\"error\":{\"code\":\"E001\",\"type\":\"TECHNICAL\",\"message\":\"test\",\"status\":500,\"action\":\"BACK_HOME\"}}";
    ErrorResponse response = new ErrorResponse(json);

    assertNotNull(response.getError());
    assertEquals("E001", response.getError().getCode());
    assertEquals(Type.TECHNICAL, response.getError().getType());
  }
}
