package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class LinkPicture extends Picture {

    @JsonProperty("largeImageMediaLink")
    private final MediaLink largeImageMediaLink;

    @JsonProperty("smallImageMediaLink")
    private final MediaLink smallImageMediaLink;

    @JsonCreator
    public LinkPicture(
            @JsonProperty("animalUuid") UUID animalUuid,
            @JsonProperty("name") String name,
            @JsonProperty("pictureType") PictureType pictureType,
            @JsonProperty("largeImageMediaLink") MediaLink largeImageMediaLink,
            @JsonProperty("smallImageMediaLink") MediaLink smallImageMediaLink
    ) {
        this.animalUuid = animalUuid;
        this.name = name;
        this.pictureType = pictureType;
        this.largeImageMediaLink = largeImageMediaLink;
        this.smallImageMediaLink = smallImageMediaLink;
    }

    @JsonIgnore
    public boolean hasUrls() {
        return largeImageMediaLink != null && smallImageMediaLink != null;
    }

    public String getLargeImageUrl() {
        return largeImageMediaLink.getUrl();
    }

    public String getSmallImageUrl() {
        return smallImageMediaLink.getUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LinkPicture that = (LinkPicture) o;

        if (largeImageMediaLink != null ? !largeImageMediaLink.equals(that.largeImageMediaLink) : that.largeImageMediaLink != null)
            return false;
        return smallImageMediaLink != null ? smallImageMediaLink.equals(that.smallImageMediaLink) : that.smallImageMediaLink == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (largeImageMediaLink != null ? largeImageMediaLink.hashCode() : 0);
        result = 31 * result + (smallImageMediaLink != null ? smallImageMediaLink.hashCode() : 0);
        return result;
    }
}
