package org.shjwfan.domain.commons;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.shjwfan.domain.utils.ParameterizedTestUtils;

class DomainTest {

  static Stream<String> checkIdMethodSource() {
    return ParameterizedTestUtils.stringMethodSource();
  }

  @ParameterizedTest
  @MethodSource("checkIdMethodSource")
  void setIdTest(String id) {
    Domain domain = new Domain();
    assertThrows(DomainException.class, () -> domain.setId(id), "id must be not blank");
  }
}
