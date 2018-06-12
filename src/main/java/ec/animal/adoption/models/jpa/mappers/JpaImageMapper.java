package ec.animal.adoption.models.jpa.mappers;

import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.models.jpa.JpaImage;
import ec.animal.adoption.models.jpa.JpaLargeImage;
import ec.animal.adoption.models.jpa.JpaSmallImage;

import java.util.Optional;

public class JpaImageMapper {

    public static Optional<JpaImage> get(Media media) {
        if(!(media instanceof MediaLink)) {
            return Optional.empty();
        }

        MediaLink mediaLink = (MediaLink) media;
        MediaMetadata mediaMetadata = mediaLink.getMediaMetadata();
        return getJpaImage(mediaLink, mediaMetadata);
    }

    private static Optional<JpaImage> getJpaImage(MediaLink mediaLink, MediaMetadata mediaMetadata) {
        if (mediaMetadata instanceof LargeImageMetadata) {
            LargeImageMetadata largeImageMetadata = (LargeImageMetadata) mediaMetadata;

            return Optional.of(new JpaLargeImage(
                    largeImageMetadata.getAnimalUuid(),
                    largeImageMetadata.getImageType().name(),
                    largeImageMetadata.getName(),
                    mediaLink.getUrl()
            ));

        } else if(mediaMetadata instanceof SmallImageMetadata) {
            SmallImageMetadata smallImageMetadata = (SmallImageMetadata) mediaMetadata;

            return Optional.of(new JpaSmallImage(
                    smallImageMetadata.getLargeImageId(),
                    mediaLink.getUrl()
            ));
        }

        return Optional.empty();
    }


}
