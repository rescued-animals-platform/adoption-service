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

package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaLinkPicture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaLinkPictureRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaLinkPictureRepository jpaLinkPictureRepository;

    private JpaAnimal jpaAnimal;

    @Before
    public void setUp() {
        jpaAnimal = createAndSaveJpaAnimal();
    }

    @Test
    public void shouldSaveLinkPicture() {
        LinkPicture linkPicture = new LinkPicture(
                jpaAnimal.toAnimal().getUuid(),
                randomAlphabetic(10),
                getRandomPictureType(),
                new MediaLink(randomAlphabetic(10)),
                new MediaLink(randomAlphabetic(10))
        );
        JpaLinkPicture entity = new JpaLinkPicture(linkPicture);

        JpaLinkPicture jpaLinkPicture = jpaLinkPictureRepository.save(entity);

        assertEquals(entity, jpaLinkPicture);
    }

    @Test
    public void shouldFindJpaPrimaryLinkPictureByAnimalUuid() {
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();
        LinkPicture linkPicture = new LinkPicture(
                animalUuid,
                randomAlphabetic(10),
                PictureType.PRIMARY,
                new MediaLink(randomAlphabetic(10)),
                new MediaLink(randomAlphabetic(10))
        );
        JpaLinkPicture jpaPrimaryLinkPicture = jpaLinkPictureRepository.save(new JpaLinkPicture(linkPicture));

        Optional<JpaLinkPicture> foundJpaPrimaryLinkPicture = jpaLinkPictureRepository.findByPictureTypeAndAnimalUuid(
                PictureType.PRIMARY.name(), animalUuid
        );

        assertTrue(foundJpaPrimaryLinkPicture.isPresent());
        assertReflectionEquals(jpaPrimaryLinkPicture, foundJpaPrimaryLinkPicture.get());
    }
}
