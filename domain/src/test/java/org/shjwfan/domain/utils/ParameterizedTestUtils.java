package org.shjwfan.domain.utils;

import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;

public class ParameterizedTestUtils {

  public static Stream<String> stringMethodSource() {
    return Stream.of(null, "");
  }

  public static Stream<String> stringMethodSource(int length) {
    return Stream.of(null, "", RandomStringUtils.random(length));
  }
}
