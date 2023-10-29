package org.shjwfan.business;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import org.shjwfan.business.commons.NamedBusinessEntity;

@Entity
public class Work extends NamedBusinessEntity {

  @SuppressWarnings("NullAway.Init")
  @Embedded
  private Content content;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "works")
  private Collection<Theme> themes = new HashSet<>();

  public Content getContent() {
    return content;
  }

  @SuppressWarnings("NullAway")
  public void setContent(@Nullable Content content) {
    checkContent(content);
    this.content = content;
  }

  public Collection<Theme> getThemes() {
    return themes;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  private void checkContent(@Nullable Content content) {
    if (content == null) {
      throw new WorkException("content must be not null");
    }
  }
}
