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

package ec.animal.adoption.repository.jpa.model;

import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.LinkPictureFactory;
import ec.animal.adoption.domain.media.PictureType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class JpaPrimaryLinkPictureTest {

    @Test
    public void shouldGenerateAnIdWhenCreatingAJpaPrimaryLinkPictureForAPrimaryLinkPictureWithNoId() {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withIdentifier(null)
                                                           .withPictureType(PictureType.PRIMARY).build();
        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = new JpaPrimaryLinkPicture(
                primaryLinkPicture, mock(JpaAnimal.class)
        );

        LinkPicture jpaPrimaryLinkPictureToLinkPicture = jpaPrimaryLinkPicture.toLinkPicture();

        assertNotNull(jpaPrimaryLinkPictureToLinkPicture.getIdentifier());
    }

    @Test
    public void shouldGenerateARegistrationDateWhenCreatingAJpaPrimaryLinkPictureForAPrimaryLinkPictureWithNoRegistrationDate() {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withRegistrationDate(null)
                                                           .withPictureType(PictureType.PRIMARY).build();
        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = new JpaPrimaryLinkPicture(
                primaryLinkPicture, mock(JpaAnimal.class)
        );

        LinkPicture jpaPrimaryLinkPictureToLinkPicture = jpaPrimaryLinkPicture.toLinkPicture();

        assertNotNull(jpaPrimaryLinkPictureToLinkPicture.getRegistrationDate());
    }

    @Test
    public void shouldCreateAPrimaryLinkPictureWithId() {
        UUID linkPictureId = UUID.randomUUID();
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withIdentifier(linkPictureId)
                                                           .withPictureType(PictureType.PRIMARY).build();
        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = new JpaPrimaryLinkPicture(
                primaryLinkPicture, mock(JpaAnimal.class)
        );

        LinkPicture jpaPrimaryLinkPictureToLinkPicture = jpaPrimaryLinkPicture.toLinkPicture();

        assertThat(jpaPrimaryLinkPictureToLinkPicture.getIdentifier(), is(linkPictureId));
    }

    @Test
    public void shouldCreateAPrimaryLinkPictureWithRegistrationDate() {
        LocalDateTime registrationDate = LocalDateTime.now();
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withRegistrationDate(registrationDate)
                                                           .withPictureType(PictureType.PRIMARY).build();
        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = new JpaPrimaryLinkPicture(
                primaryLinkPicture, mock(JpaAnimal.class)
        );

        LinkPicture jpaPrimaryLinkPictureToLinkPicture = jpaPrimaryLinkPicture.toLinkPicture();

        assertThat(jpaPrimaryLinkPictureToLinkPicture.getRegistrationDate(), is(registrationDate));
    }

    @Test
    public void shouldCreateJpaPrimaryLinkPictureFromPrimaryLinkPicture() {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = new JpaPrimaryLinkPicture(
                primaryLinkPicture, mock(JpaAnimal.class)
        );

        LinkPicture jpaPrimaryLinkPictureToLinkPicture = jpaPrimaryLinkPicture.toLinkPicture();

        assertThat(jpaPrimaryLinkPictureToLinkPicture.getName(), is(primaryLinkPicture.getName()));
        assertThat(jpaPrimaryLinkPictureToLinkPicture.getPictureType(), is(primaryLinkPicture.getPictureType()));
        assertThat(jpaPrimaryLinkPictureToLinkPicture.getLargeImagePublicId(), is(primaryLinkPicture.getLargeImagePublicId()));
        assertThat(jpaPrimaryLinkPictureToLinkPicture.getLargeImageUrl(), is(primaryLinkPicture.getLargeImageUrl()));
        assertThat(jpaPrimaryLinkPictureToLinkPicture.getSmallImagePublicId(), is(primaryLinkPicture.getSmallImagePublicId()));
        assertThat(jpaPrimaryLinkPictureToLinkPicture.getSmallImageUrl(), is(primaryLinkPicture.getSmallImageUrl()));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaPrimaryLinkPicture.class).usingGetClass()
                      .withPrefabValues(JpaAnimal.class, mock(JpaAnimal.class), mock(JpaAnimal.class))
                      .suppress(Warning.NONFINAL_FIELDS, Warning.REFERENCE_EQUALITY, Warning.SURROGATE_KEY).verify();
    }
}