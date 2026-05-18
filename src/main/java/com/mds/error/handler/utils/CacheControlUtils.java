package com.mds.error.handler.utils;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.CacheControl;

/**
 * Utility class for creating {@link CacheControl} directives used in error responses.
 *
 * <p>Error responses are served with {@code max-age=0} to prevent caching by clients and proxies.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CacheControlUtils {

  public static CacheControl createCacheControl() {
    return CacheControl.maxAge(0L, TimeUnit.SECONDS);
  }
}
