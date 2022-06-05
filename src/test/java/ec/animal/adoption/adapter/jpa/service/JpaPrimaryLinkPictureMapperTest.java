package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.adapter.jpa.model.JpaPrimaryLinkPicture;
import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.media.LinkPictureFactory;
import ec.animal.adoption.domain.model.media.PictureType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.mock;

class JpaPrimaryLinkPictureMapperTest {

    @Test
    void shouldGenerateAnIdWhenCreatingAJpaPrimaryLinkPictureForAPrimaryLinkPictureWithNoId() {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withIdentifier(null)
                                                           .withPictureType(PictureType.PRIMARY).build();

        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = JpaPrimaryLinkPictureMapper.MAPPER.toJpaPrimaryLinkPicture(
                primaryLinkPicture, mock(JpaAnimal.class)
        );

        assertNotNull(jpaPrimaryLinkPicture.getId());
    }

    @Test
    void shouldGenerateARegistrationDateWhenCreatingAJpaPrimaryLinkPictureForAPrimaryLinkPictureWithNoRegistrationDate() {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withRegistrationDate(null)
                                                           .withPictureType(PictureType.PRIMARY).build();

        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = JpaPrimaryLinkPictureMapper.MAPPER.toJpaPrimaryLinkPicture(
                primaryLinkPicture, mock(JpaAnimal.class)
        );

        assertNotNull(jpaPrimaryLinkPicture.getRegistrationDate());
    }

    @Test
    void shouldCreateJpaPrimaryLinkPictureFromPrimaryLinkPictureAndJpaAnimal() {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        JpaAnimal jpaAnimal = mock(JpaAnimal.class);

        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = JpaPrimaryLinkPictureMapper.MAPPER.toJpaPrimaryLinkPicture(
                primaryLinkPicture, jpaAnimal
        );

        assertThat(jpaPrimaryLinkPicture.getId(), is(primaryLinkPicture.getIdentifier()));
        assertThat(jpaPrimaryLinkPicture.getRegistrationDate(), is(primaryLinkPicture.getRegistrationDate()));
        assertThat(jpaPrimaryLinkPicture.getName(), is(primaryLinkPicture.getName()));
        assertThat(jpaPrimaryLinkPicture.getLargeImagePublicId(), is(primaryLinkPicture.getLargeImagePublicId()));
        assertThat(jpaPrimaryLinkPicture.getLargeImageUrl(), is(primaryLinkPicture.getLargeImageUrl()));
        assertThat(jpaPrimaryLinkPicture.getSmallImagePublicId(), is(primaryLinkPicture.getSmallImagePublicId()));
        assertThat(jpaPrimaryLinkPicture.getSmallImageUrl(), is(primaryLinkPicture.getSmallImageUrl()));
        assertThat(jpaPrimaryLinkPicture.getJpaAnimal(), is(notNullValue()));
        assertThat(jpaPrimaryLinkPicture.getJpaAnimal(), is(jpaAnimal));
    }

    @Test
    void shouldCreatePrimaryLinkPictureFromJpaPrimaryLinkPicture() {
        UUID id = UUID.randomUUID();
        JpaAnimal jpaAnimal = mock(JpaAnimal.class);
        LocalDateTime registrationDate = LocalDateTime.now();
        String name = randomAlphabetic(10);
        String largeImagePublicId = randomAlphabetic(10);
        String largeImageUrl = randomAlphabetic(10);
        String smallImagePublicId = randomAlphabetic(10);
        String smallImageUrl = randomAlphabetic(10);
        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = new JpaPrimaryLinkPicture(id,
                                                                                jpaAnimal,
                                                                                registrationDate,
                                                                                name,
                                                                                largeImagePublicId,
                                                                                largeImageUrl,
                                                                                smallImagePublicId,
                                                                                smallImageUrl);

        LinkPicture linkPicture = JpaPrimaryLinkPictureMapper.MAPPER.toLinkPicture(jpaPrimaryLinkPicture);

        assertThat(linkPicture.getIdentifier(), is(id));
        assertThat(linkPicture.getRegistrationDate(), is(registrationDate));
        assertThat(linkPicture.getPictureType(), is(PictureType.PRIMARY));
        assertThat(linkPicture.getName(), is(name));
        assertThat(linkPicture.getLargeImagePublicId(), is(largeImagePublicId));
        assertThat(linkPicture.getLargeImageUrl(), is(largeImageUrl));
        assertThat(linkPicture.getSmallImagePublicId(), is(smallImagePublicId));
        assertThat(linkPicture.getSmallImageUrl(), is(smallImageUrl));
    }

    @Test
    void shouldReturnNullJpaPrimaryLinkPictureIfBothLinkPictureAndJpaAnimalAreNull() {
        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = JpaPrimaryLinkPictureMapper.MAPPER.toJpaPrimaryLinkPicture(
                null, null
        );

        assertNull(jpaPrimaryLinkPicture);
    }
}