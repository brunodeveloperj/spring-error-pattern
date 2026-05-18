package com.mds.error.handler.exception.keys;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Constant holder for HTTP status codes used in error responses.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorStatusKeys {

  public static final int UNPROCESSABLE_ENTITY = HttpStatus.UNPROCESSABLE_ENTITY.value();
  public static final int NOT_FOUND = HttpStatus.NOT_FOUND.value();
  public static final int METHOD_FAILURE = HttpStatus.INTERNAL_SERVER_ERROR.value();
}
