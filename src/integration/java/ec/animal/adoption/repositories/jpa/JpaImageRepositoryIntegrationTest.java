package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.media.ImageType;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaImage;
import ec.animal.adoption.models.jpa.JpaLargeImage;
import ec.animal.adoption.models.jpa.JpaSmallImage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JpaImageRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaImageRepository jpaImageRepository;

    @Test
    public void shouldSaveALargeImage() {
        JpaAnimal jpaAnimal = createAndSaveJpaAnimal();
        JpaLargeImage entity = new JpaLargeImage(
                jpaAnimal.toAnimal().getUuid(),
                ImageType.PRIMARY.name(),
                randomAlphabetic(10),
                randomAlphabetic(10)
        );

        JpaImage savedJpaLargeImage = jpaImageRepository.save(entity);

        assertThat(savedJpaLargeImage, is(entity));
    }

    @Test
    public void shouldSaveASmallImage() {
        JpaAnimal jpaAnimal = createAndSaveJpaAnimal();
        JpaLargeImage jpaLargeImage = jpaImageRepository.save(new JpaLargeImage(
                jpaAnimal.toAnimal().getUuid(),
                ImageType.PRIMARY.name(),
                randomAlphabetic(10),
                randomAlphabetic(10)
        ));
        JpaSmallImage entity = new JpaSmallImage(jpaLargeImage.getId(), randomAlphabetic(10));

        JpaImage savedJpaSmallImage = jpaImageRepository.save(entity);

        assertThat(savedJpaSmallImage, is(entity));
    }
}
