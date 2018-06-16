package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class LinkPicture extends Picture {

    private final MediaLink largeImageMediaLink;
    private final MediaLink smallImageMediaLink;

    public LinkPicture(
            UUID animalUuid,
            String name,
            PictureType pictureType,
            MediaLink largeImageMediaLink,
            MediaLink smallImageMediaLink
    ) {
        this.animalUuid = animalUuid;
        this.name = name;
        this.pictureType = pictureType;
        this.largeImageMediaLink = largeImageMediaLink;
        this.smallImageMediaLink = smallImageMediaLink;
    }

    @JsonIgnore
    public boolean hasUrls() {
        return true;
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
