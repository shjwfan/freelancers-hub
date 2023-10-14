package org.shjwfan.domain.commons;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@MappedSuperclass
public class Domain {

  @SuppressWarnings("NullAway.Init")
  @Id
  protected String id;

  public String getId() {
    return id;
  }

  @SuppressWarnings("NullAway")
  public void setId(@Nullable String id) {
    checkId(id);
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Domain domain = (Domain) o;
    return new EqualsBuilder().append(id, domain.id)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id)
        .toHashCode();
  }

  private void checkId(@Nullable String id) {
    if (StringUtils.isBlank(id)) {
      throw new DomainException("id must be not blank");
    }
  }
}
