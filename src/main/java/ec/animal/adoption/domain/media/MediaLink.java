package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class MediaLink {

    @JsonIgnore
    private UUID animalUuid;

    @JsonIgnore
    private String mediaName;

    private final String url;

    public MediaLink(UUID animalUuid, String mediaName, String url) {
        this.animalUuid = animalUuid;
        this.mediaName = mediaName;
        this.url = url;
    }

    @JsonCreator
    public MediaLink(@JsonProperty("url") String url) {
        this.url = url;
    }

    public UUID getAnimalUuid() {
        return animalUuid;
    }

    public String getMediaName() {
        return mediaName;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaLink mediaLink = (MediaLink) o;

        if (animalUuid != null ? !animalUuid.equals(mediaLink.animalUuid) : mediaLink.animalUuid != null) return false;
        if (mediaName != null ? !mediaName.equals(mediaLink.mediaName) : mediaLink.mediaName != null) return false;
        return url != null ? url.equals(mediaLink.url) : mediaLink.url == null;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (mediaName != null ? mediaName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
