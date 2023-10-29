package org.shjwfan.business;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import org.shjwfan.business.commons.NamedBusinessEntity;

@Entity
public class Pack extends NamedBusinessEntity {

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "pack_themes", joinColumns = @JoinColumn(name = "pack_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "theme_id", nullable = false))
  private Collection<Theme> themes = new HashSet<>();

  public Collection<Theme> getThemes() {
    return themes;
  }

  public void addTheme(Theme theme) {
    if (!themes.add(theme)) {
      throw new PackException(String.format("pack %s, already contains theme %s", id, theme.getId()));
    }
  }

  public void removeTheme(Theme theme) {
    if (!themes.remove(theme)) {
      throw new PackException(String.format("pack %s, doesn't contains theme %s", id, theme.getId()));
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
}
