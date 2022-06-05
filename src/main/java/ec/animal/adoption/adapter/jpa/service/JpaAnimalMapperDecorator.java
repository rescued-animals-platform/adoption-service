package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.domain.model.animal.Animal;

public abstract class JpaAnimalMapperDecorator implements JpaAnimalMapper {

    private final JpaAnimalMapper delegate;

    protected JpaAnimalMapperDecorator(JpaAnimalMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public JpaAnimal toJpaAnimal(Animal animal) {
        JpaAnimal jpaAnimal = this.delegate.toJpaAnimal(animal);

        animal.getPrimaryLinkPicture().ifPresent(linkPicture -> jpaAnimal.setJpaPrimaryLinkPicture(
                JpaPrimaryLinkPictureMapper.MAPPER.toJpaPrimaryLinkPicture(linkPicture, jpaAnimal)
        ));

        animal.getCharacteristics().ifPresent(characteristics -> jpaAnimal.setJpaCharacteristics(
                JpaCharacteristicsMapper.MAPPER.toJpaCharacteristics(characteristics, jpaAnimal)
        ));

        animal.getStory().ifPresent(story -> jpaAnimal.setJpaStory(JpaStoryMapper.MAPPER.toJpaStory(story, jpaAnimal)));

        return jpaAnimal;
    }
}
