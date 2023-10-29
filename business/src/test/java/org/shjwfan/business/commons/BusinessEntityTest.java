package org.shjwfan.business.commons;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.shjwfan.business.utils.ParameterizedTestUtils;

class BusinessEntityTest {

  static Stream<String> checkIdMethodSource() {
    return ParameterizedTestUtils.stringMethodSource();
  }

  @ParameterizedTest
  @MethodSource("checkIdMethodSource")
  void setIdTest(String id) {
    BusinessEntity businessEntity = new BusinessEntity();
    assertThrows(BusinessException.class, () -> businessEntity.setId(id), "id must be not blank");
  }
}
