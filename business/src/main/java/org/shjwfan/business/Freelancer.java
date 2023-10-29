package org.shjwfan.business;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import org.apache.commons.lang3.StringUtils;
import org.shjwfan.business.commons.BusinessEntity;

@Entity
public class Freelancer extends BusinessEntity {

  @SuppressWarnings("NullAway.Init")
  @Column(name = "username", nullable = false, length = 32)
  private String username;

  @SuppressWarnings("NullAway.Init")
  @Column(name = "password", nullable = false, length = 512)
  private String password;

  @SuppressWarnings("NullAway.Init")
  @Column(name = "first_name", nullable = false, length = 32)
  private String firstName;

  @SuppressWarnings("NullAway.Init")
  @Column(name = "last_name", nullable = false, length = 32)
  private String lastName;

  @SuppressWarnings("NullAway.Init")
  @Column(name = "bio_0", nullable = false, length = 512)
  private String bio0;

  @SuppressWarnings("NullAway.Init")
  @Column(name = "bio_1", nullable = false, length = 512)
  private String bio1;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinTable(name = "freelancer_packs", joinColumns = @JoinColumn(name = "freelancer_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "pack_id", nullable = false))
  private Collection<Pack> packs = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinTable(name = "freelancer_themes", joinColumns = @JoinColumn(name = "freelancer_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "theme_id", nullable = false))
  private Collection<Theme> themes = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinTable(name = "freelancer_works", joinColumns = @JoinColumn(name = "freelancer_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "work_id", nullable = false))
  private Collection<Work> works = new HashSet<>();

  public String getUsername() {
    return username;
  }

  @SuppressWarnings("NullAway")
  public void setUsername(@Nullable String username) {
    checkUsername(username);
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  @SuppressWarnings("NullAway")
  public void setPassword(@Nullable String password) {
    checkPassword(password);
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  @SuppressWarnings("NullAway")
  public void setFirstName(@Nullable String firstName) {
    checkFirstName(firstName);
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  @SuppressWarnings("NullAway")
  public void setLastName(@Nullable String lastName) {
    checkLastName(lastName);
    this.lastName = lastName;
  }

  public String getBio0() {
    return bio0;
  }

  @SuppressWarnings("NullAway")
  public void setBio0(@Nullable String bio0) {
    checkBio(bio0);
    this.bio0 = bio0;
  }

  public String getBio1() {
    return bio1;
  }

  @SuppressWarnings("NullAway")
  public void setBio1(@Nullable String bio1) {
    checkBio(bio1);
    this.bio1 = bio1;
  }

  public Collection<Pack> getPacks() {
    return packs;
  }

  public void addPack(Pack pack) {
    if (!packs.add(pack)) {
      throw new FreelancerException(String.format("freelancer %s, already contains pack %s", id, pack.getId()));
    }
  }

  public void removePack(Pack pack) {
    if (!packs.remove(pack)) {
      throw new FreelancerException(String.format("freelancer %s, doesn't contains pack %s", id, pack.getId()));
    }
  }

  public Collection<Theme> getThemes() {
    return themes;
  }

  public void addTheme(Theme theme) {
    if (!themes.add(theme)) {
      throw new FreelancerException(String.format("freelancer %s, already contains theme %s", id, theme.getId()));
    }
  }

  public void removeTheme(Theme theme) {
    if (!themes.remove(theme)) {
      throw new FreelancerException(String.format("freelancer %s, doesn't contains theme %s", id, theme.getId()));
    }
  }

  public Collection<Work> getWorks() {
    return works;
  }

  public void addWork(Work work) {
    if (!works.add(work)) {
      throw new FreelancerException(String.format("freelancer %s, already contains work %s", id, work.getId()));
    }
  }

  public void removeWork(Work work) {
    if (!works.remove(work)) {
      throw new FreelancerException(String.format("freelancer %s, doesn't contains work %s", id, work.getId()));
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

  private void checkUsername(@Nullable String username) {
    if (StringUtils.isBlank(username) || username.length() > 32) {
      throw new FreelancerException("username must be not blank, username length must be between 1 and 32 characters");
    }
  }

  private void checkPassword(@Nullable String password) {
    if (StringUtils.isBlank(password) || password.length() > 512) {
      throw new FreelancerException("password must be not blank, password length must be between 1 and 512 characters");
    }
  }

  private void checkFirstName(@Nullable String firstName) {
    if (StringUtils.isBlank(firstName) || firstName.length() > 32) {
      throw new FreelancerException("first name must be not blank, first name length must be between 1 and 32 characters");
    }
  }

  private void checkLastName(@Nullable String lastName) {
    if (StringUtils.isBlank(lastName) || lastName.length() > 32) {
      throw new FreelancerException("last name must be not blank, last name length must be between 1 and 32 characters");
    }
  }

  private void checkBio(@Nullable String bio) {
    if (StringUtils.isBlank(bio) || bio.length() > 512) {
      throw new FreelancerException("bio must be not blank, bio length must be between 1 and 512 characters");
    }
  }
}
