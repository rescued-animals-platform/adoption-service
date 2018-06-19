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
import ec.animal.adoption.domain.Story;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaStory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

public class JpaStoryRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaStoryRepository jpaStoryRepository;

    private JpaAnimal jpaAnimal;
    private JpaStory entity;

    @Before
    public void setUp() {
        jpaAnimal = createAndSaveJpaAnimal();
    }

    @Test
    public void shouldSaveStory() {
        Story story = new Story(randomAlphabetic(100));
        story.setAnimalUuid(jpaAnimal.toAnimal().getUuid());
        entity = new JpaStory(story);

        JpaStory jpaStory = jpaStoryRepository.save(entity);

        assertEquals(entity, jpaStory);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldThrowDataIntegrityViolationExceptionWhenCreationStoryForSameAnimal() {
        Story story = new Story(randomAlphabetic(100));
        story.setAnimalUuid(jpaAnimal.toAnimal().getUuid());
        entity = new JpaStory(story);
        jpaStoryRepository.save(entity);

        jpaStoryRepository.save(new JpaStory(story));
    }
}
