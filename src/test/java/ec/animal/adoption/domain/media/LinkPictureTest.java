/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.domain.media;

import ec.animal.adoption.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;

public class LinkPictureTest {

    private String name;
    private PictureType pictureType;
    private String largeImagePublicId;
    private String largeImageUrl;
    private String smallImagePublicId;
    private String smallImageUrl;
    private MediaLink largeImageMediaLink;
    private MediaLink smallImageMediaLink;

    @BeforeEach
    void setUp() {
        name = randomAlphabetic(10);
        pictureType = TestUtils.getRandomPictureType();
        largeImagePublicId = randomAlphabetic(10);
        largeImageUrl = randomAlphabetic(30);
        smallImagePublicId = randomAlphabetic(10);
        smallImageUrl = randomAlphabetic(30);
        largeImageMediaLink = new MediaLink(largeImagePublicId, largeImageUrl);
        smallImageMediaLink = new MediaLink(smallImagePublicId, smallImageUrl);
    }

    @Test
    public void shouldCreateLinkPictureWithNoIdentifierNorRegistrationDate() {
        LinkPicture linkPicture = new LinkPicture(name, pictureType, largeImageMediaLink, smallImageMediaLink);

        assertNull(linkPicture.getIdentifier());
        assertNull(linkPicture.getRegistrationDate());
        assertEquals(name, linkPicture.getName());
        assertEquals(pictureType, linkPicture.getPictureType());
        assertEquals(largeImagePublicId, linkPicture.getLargeImagePublicId());
        assertEquals(largeImageUrl, linkPicture.getLargeImageUrl());
        assertEquals(smallImagePublicId, linkPicture.getSmallImagePublicId());
        assertEquals(smallImageUrl, linkPicture.getSmallImageUrl());
    }

    @Test
    public void shouldCreateLinkPictureWithIdentifierAndRegistrationDate() {
        UUID linkPictureId = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();

        LinkPicture linkPicture = new LinkPicture(linkPictureId,
                                                  registrationDate,
                                                  name,
                                                  pictureType,
                                                  largeImageMediaLink,
                                                  smallImageMediaLink);

        assertEquals(linkPictureId, linkPicture.getIdentifier());
        assertEquals(registrationDate, linkPicture.getRegistrationDate());
        assertEquals(name, linkPicture.getName());
        assertEquals(pictureType, linkPicture.getPictureType());
        assertEquals(largeImagePublicId, linkPicture.getLargeImagePublicId());
        assertEquals(largeImageUrl, linkPicture.getLargeImageUrl());
        assertEquals(smallImagePublicId, linkPicture.getSmallImagePublicId());
        assertEquals(smallImageUrl, linkPicture.getSmallImageUrl());
    }

    @Test
    void shouldReturnTrueWhenItIsPrimary() {
        LinkPicture linkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();

        boolean isPrimary = linkPicture.isPrimary();

        assertTrue(isPrimary);
    }

    @Test
    void shouldReturnFalseWhenItIsNotPrimary() {
        LinkPicture linkPicture = LinkPictureFactory.random().withPictureType(PictureType.ALTERNATE).build();

        boolean isPrimary = linkPicture.isPrimary();

        assertFalse(isPrimary);
    }

    @Test
    void shouldUpdateNamePictureTypeLargeImageMediaLinkAndSmallImageMediaLinkInLinkPicture() {
        LinkPicture linkPicture = LinkPictureFactory.random().build();
        LinkPicture newLinkPicture = new LinkPicture(name, pictureType, largeImageMediaLink, smallImageMediaLink);

        LinkPicture updatedLinkPicture = linkPicture.updateWith(newLinkPicture);

        assertAll(
                () -> assertEquals(linkPicture.getIdentifier(), updatedLinkPicture.getIdentifier()),
                () -> assertEquals(linkPicture.getRegistrationDate(), updatedLinkPicture.getRegistrationDate()),
                () -> assertEquals(name, updatedLinkPicture.getName()),
                () -> assertEquals(pictureType, updatedLinkPicture.getPictureType()),
                () -> assertEquals(largeImagePublicId, updatedLinkPicture.getLargeImagePublicId()),
                () -> assertEquals(largeImageUrl, updatedLinkPicture.getLargeImageUrl()),
                () -> assertEquals(smallImagePublicId, updatedLinkPicture.getSmallImagePublicId()),
                () -> assertEquals(smallImageUrl, updatedLinkPicture.getSmallImageUrl())
        );
    }

    @Test
    void shouldReturnSameLinkPictureWhenUpdatedWithItself() {
        LinkPicture linkPicture = LinkPictureFactory.random().build();

        LinkPicture updatedLinkPicture = linkPicture.updateWith(linkPicture);

        assertEquals(linkPicture, updatedLinkPicture);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingLinkPictureThatHasNoIdentifier() {
        LinkPicture linkPicture = LinkPictureFactory.random().withIdentifier(null).build();

        assertThrows(IllegalArgumentException.class, () -> {
            linkPicture.updateWith(LinkPictureFactory.random().build());
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingLinkPictureThatHasNoRegistrationDate() {
        LinkPicture linkPicture = LinkPictureFactory.random().withRegistrationDate(null).build();

        assertThrows(IllegalArgumentException.class, () -> {
            linkPicture.updateWith(LinkPictureFactory.random().build());
        });
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(LinkPicture.class).usingGetClass().verify();
    }
}