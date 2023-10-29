package org.shjwfan.business;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class Content {

  public static final Map<String, String> MEDIA_TYPE_2_EXTENSION = Map.of("image/gif", "gif", "image/jpeg", "jpeg", "image/png", "png", "text/html", "html");

  @SuppressWarnings("NullAway.Init")
  @Column(name = "path", length = 512)
  private String path;

  @SuppressWarnings("NullAway.Init")
  @Column(name = "media_type", length = 32)
  private String mediaType;

  public String getPath() {
    return path;
  }

  @SuppressWarnings("NullAway")
  public void setPath(@Nullable String path) {
    checkPath(path);
    this.path = path;
  }

  public String getMediaType() {
    return mediaType;
  }

  @SuppressWarnings("NullAway")
  public void setMediaType(@Nullable String mediaType) {
    checkMediaType(mediaType);
    this.mediaType = mediaType;
  }

  public boolean isEmpty() {
    return StringUtils.isAllBlank(path, mediaType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Content content = (Content) o;
    return new EqualsBuilder().append(path, content.path)
        .append(mediaType, content.mediaType)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(path)
        .append(mediaType)
        .toHashCode();
  }

  private void checkPath(@Nullable String path) {
    if (StringUtils.isBlank(path) || path.length() > 512) {
      throw new ContentException("path must be not blank, path length must be between 1 and 512 characters");
    }
  }

  private void checkMediaType(@Nullable String mediaType) {
    Set<String> mediaTypes = MEDIA_TYPE_2_EXTENSION.keySet();
    if (mediaType == null || !mediaTypes.contains(mediaType)) {
      throw new ContentException(String.format("unsupported media type %s, only %s are supported", mediaType, String.join(", ", mediaTypes)));
    }
  }
}
