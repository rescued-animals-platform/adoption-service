package ec.animal.adoption.repository.jpa;

import ec.animal.adoption.domain.animal.AnimalFactory;
import ec.animal.adoption.repository.jpa.model.JpaAnimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractJpaRepositoryIntegrationTest {

    @Autowired
    JpaAnimalRepository jpaAnimalRepository;

    JpaAnimal createAndSaveJpaAnimalForDefaultOrganization() {
        return jpaAnimalRepository.save(new JpaAnimal(AnimalFactory.randomWithDefaultOrganization().build()));
    }

    JpaAnimal createAndSaveJpaAnimalForAnotherOrganization() {
        return jpaAnimalRepository.save(new JpaAnimal(AnimalFactory.randomWithAnotherOrganization().build()));
    }
}
