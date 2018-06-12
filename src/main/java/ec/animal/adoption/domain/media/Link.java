package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Link {

    private final UUID animalUuid;
    private final String mediaName;
    private final String url;

    @JsonCreator
    public Link(
            @JsonProperty("animalUuid") UUID animalUuid,
            @JsonProperty("mediaName") String mediaName,
            @JsonProperty("url") String url
    ) {
        this.animalUuid = animalUuid;
        this.mediaName = mediaName;
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

        Link link = (Link) o;

        if (animalUuid != null ? !animalUuid.equals(link.animalUuid) : link.animalUuid != null) return false;
        if (mediaName != null ? !mediaName.equals(link.mediaName) : link.mediaName != null) return false;
        return url != null ? url.equals(link.url) : link.url == null;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (mediaName != null ? mediaName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
