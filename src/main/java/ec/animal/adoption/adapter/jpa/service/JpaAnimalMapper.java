package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.state.State;
import ec.animal.adoption.domain.model.state.StateName;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        JpaOrganizationMapper.class,
        JpaCharacteristicsMapper.class,
        JpaStoryMapper.class,
        JpaPrimaryLinkPictureMapper.class
})
@DecoratedWith(JpaAnimalMapperDecorator.class)
public interface JpaAnimalMapper {

    JpaAnimalMapper MAPPER = Mappers.getMapper(JpaAnimalMapper.class);

    @Mapping(source = "id", target = "identifier")
    @Mapping(source = "jpaAnimal", target = "state", qualifiedByName = "toState")
    @Mapping(source = "jpaPrimaryLinkPicture", target = "primaryLinkPicture")
    @Mapping(source = "jpaCharacteristics", target = "characteristics")
    @Mapping(source = "jpaStory", target = "story")
    @Mapping(source = "jpaOrganization", target = "organization")
    @Mapping(target = "updateWith", ignore = true)
    Animal toAnimal(JpaAnimal jpaAnimal);

    @Named("toState")
    default State toState(JpaAnimal jpaAnimal) {
        return State.from(StateName.valueOf(jpaAnimal.getStateName()),
                          jpaAnimal.getAdoptionFormId(),
                          jpaAnimal.getUnavailableStateNotes());
    }

    @Mapping(source = "identifier", target = "id")
    @Mapping(expression = "java( animal.getState().getAdoptionFormId().orElse(null) )", target = "adoptionFormId")
    @Mapping(expression = "java( animal.getState().getNotes().orElse(null) )", target = "unavailableStateNotes")
    @Mapping(source = "animal.organization", target = "jpaOrganization")
    @Mapping(target = "jpaPrimaryLinkPicture", ignore = true)
    @Mapping(target = "jpaCharacteristics", ignore = true)
    @Mapping(target = "jpaStory", ignore = true)
    JpaAnimal toJpaAnimal(Animal animal);

    @Mapping(target = "id", expression = "java( UUID.randomUUID() )")
    @Mapping(target = "registrationDate", expression = "java( LocalDateTime.now() )")
    @Mapping(source = "animalDto.stateNameAsString", target = "stateName")
    @Mapping(expression = "java( animalDto.getAdoptionFormId().orElse(null) )", target = "adoptionFormId")
    @Mapping(expression = "java( animalDto.getNotes().orElse(null) )", target = "unavailableStateNotes")
    @Mapping(source = "animalDto.organization", target = "jpaOrganization")
    @Mapping(target = "jpaPrimaryLinkPicture", ignore = true)
    @Mapping(target = "jpaCharacteristics", ignore = true)
    @Mapping(target = "jpaStory", ignore = true)
    JpaAnimal toJpaAnimal(AnimalDto animalDto);
}
