package ec.animal.adoption.models.jpa.mappers;

import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.models.jpa.JpaImage;
import ec.animal.adoption.models.jpa.JpaLargeImage;
import ec.animal.adoption.models.jpa.JpaSmallImage;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomImageType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class JpaImageMapperTest {

    private String url;

    @Before
    public void setUp() {
        url = randomAlphabetic(10);
    }

    @Test
    public void shouldReturnEmptyWhenMediaIsNotInstanceOfMediaLink() {
        Media media = mock(Media.class);

        Optional<JpaImage> optionalJpaImage = JpaImageMapper.get(media);

        assertThat(optionalJpaImage.isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyWhenMediaMetadataIsNotInstanceOfLargeImageMetadataNorSmallImageMetadata() {
        MediaMetadata mediaMetadata = mock(MediaMetadata.class);
        Media media = new MediaLink(url, mediaMetadata);

        Optional<JpaImage> optionalJpaImage = JpaImageMapper.get(media);

        assertThat(optionalJpaImage.isPresent(), is(false));
    }

    @Test
    public void shouldReturnJpaLargeImageWithMediaData() {
        UUID animalUuid = UUID.randomUUID();
        String name = randomAlphabetic(10);
        ImageType imageType = getRandomImageType();
        MediaMetadata largeImageMetadata = new LargeImageMetadata(animalUuid, name, imageType);
        Media media = new MediaLink(url, largeImageMetadata);

        Optional<JpaImage> optionalJpaImage = JpaImageMapper.get(media);

        assertThat(optionalJpaImage.isPresent(), is(true));
        JpaImage jpaImage = optionalJpaImage.get();
        assertThat(jpaImage, is(instanceOf(JpaLargeImage.class)));
        JpaLargeImage jpaLargeImage = (JpaLargeImage) jpaImage;
        assertThat(jpaLargeImage.getAnimalUuid(), is(animalUuid));
        assertThat(jpaLargeImage.getImageType(), is(imageType.name()));
        assertThat(jpaLargeImage.getName(), is(name));
        assertThat(jpaLargeImage.getUrl(), is(url));
    }

    @Test
    public void shouldReturnJpaSmallImageWithMediaData() {
        UUID largeImageId = UUID.randomUUID();
        MediaMetadata smallImageMetadata = new SmallImageMetadata(largeImageId);
        Media media = new MediaLink(url, smallImageMetadata);

        Optional<JpaImage> optionalJpaImage = JpaImageMapper.get(media);

        assertThat(optionalJpaImage.isPresent(), is(true));
        JpaImage jpaImage = optionalJpaImage.get();
        assertThat(jpaImage, is(instanceOf(JpaSmallImage.class)));
        JpaSmallImage jpaSmallImage = (JpaSmallImage) jpaImage;
        assertThat(jpaSmallImage.getLargeImageId(), is(largeImageId));
        assertThat(jpaSmallImage.getUrl(), is(url));
    }
}