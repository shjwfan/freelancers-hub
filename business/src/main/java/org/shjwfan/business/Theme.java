package org.shjwfan.business;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import org.shjwfan.business.commons.NamedBusinessEntity;

@Entity
public class Theme extends NamedBusinessEntity {

  @SuppressWarnings("NullAway.Init")
  @Embedded
  private Content content;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "themes")
  private Collection<Pack> packs = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "themes_work", joinColumns = @JoinColumn(name = "theme_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "work_id", nullable = false))
  private Collection<Work> works = new HashSet<>();

  public Content getContent() {
    return content;
  }

  @SuppressWarnings("NullAway")
  public void setContent(@Nullable Content content) {
    checkContent(content);
    this.content = content;
  }

  public Collection<Pack> getPacks() {
    return packs;
  }

  public Collection<Work> getWorks() {
    return works;
  }

  public void addWork(Work work) {
    if (!works.add(work)) {
      throw new ThemeException(String.format("theme %s, already contains work %s", id, work.getId()));
    }
  }

  public void removeWork(Work work) {
    if (!works.remove(work)) {
      throw new ThemeException(String.format("theme %s, doesn't contains work %s", id, work.getId()));
    }
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
      throw new ThemeException("content must be not null");
    }
  }
}
