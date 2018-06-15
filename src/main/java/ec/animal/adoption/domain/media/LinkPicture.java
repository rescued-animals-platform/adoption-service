package ec.animal.adoption.domain.media;

import java.util.UUID;

public class LinkPicture implements Picture {
    private final UUID animalUuid;
    private final String name;
    private final PictureType pictureType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LinkPicture that = (LinkPicture) o;

        if (animalUuid != null ? !animalUuid.equals(that.animalUuid) : that.animalUuid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pictureType != that.pictureType) return false;
        if (largeImageMediaLink != null ? !largeImageMediaLink.equals(that.largeImageMediaLink) : that.largeImageMediaLink != null)
            return false;
        return smallImageMediaLink != null ? smallImageMediaLink.equals(that.smallImageMediaLink) : that.smallImageMediaLink == null;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pictureType != null ? pictureType.hashCode() : 0);
        result = 31 * result + (largeImageMediaLink != null ? largeImageMediaLink.hashCode() : 0);
        result = 31 * result + (smallImageMediaLink != null ? smallImageMediaLink.hashCode() : 0);
        return result;
    }
}
