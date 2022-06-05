package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.AnimalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class AbstractJpaRepositoryIntegrationTest {

    @Autowired
    JpaAnimalRepository jpaAnimalRepository;

    JpaAnimal createAndSaveJpaAnimalForDefaultOrganization() {
        Animal animal = AnimalFactory.randomWithDefaultOrganization().build();
        return jpaAnimalRepository.save(JpaAnimalMapper.MAPPER.toJpaAnimal(animal));
    }

    JpaAnimal createAndSaveJpaAnimalForAnotherOrganization() {
        Animal animal = AnimalFactory.randomWithAnotherOrganization().build();
        return jpaAnimalRepository.save(JpaAnimalMapper.MAPPER.toJpaAnimal(animal));
    }
}
