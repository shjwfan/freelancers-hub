package org.shjwfan.business.commons;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public class NamedBusinessEntity extends BusinessEntity {

  @SuppressWarnings("NullAway.Init")
  @Column(name = "name", nullable = false, length = 32)
  private String name;

  public String getName() {
    return name;
  }

  @SuppressWarnings("NullAway")
  public void setName(@Nullable String name) {
    checkName(name);
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  private void checkName(@Nullable String name) {
    if (StringUtils.isBlank(name) || name.length() > 32) {
      throw new BusinessException("name must be not blank, name length must be between 1 and 32 characters");
    }
  }
}
