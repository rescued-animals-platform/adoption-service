package ec.animal.adoption.domain.picture;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "mediaLink")
public class MediaLink implements PictureRepresentation {

    private final String url;

    @JsonCreator
    public MediaLink(@JsonProperty("url") String url) {
        this.url = url;
    }

    String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaLink mediaLink = (MediaLink) o;

        return this.url != null ? this.url.equals(mediaLink.url) : mediaLink.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
