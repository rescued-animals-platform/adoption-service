package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.adapter.jpa.model.JpaStory;
import ec.animal.adoption.domain.model.story.Story;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JpaStoryMapper {

    JpaStoryMapper MAPPER = Mappers.getMapper(JpaStoryMapper.class);

    @Mapping(source = "id", target = "identifier")
    @Mapping(target = "updateWith", ignore = true)
    Story toStory(JpaStory jpaStory);

    @Mapping(source = "story.identifier", target = "id", defaultExpression = "java( UUID.randomUUID() )")
    @Mapping(source = "story.registrationDate", target = "registrationDate", defaultExpression = "java( LocalDateTime.now() )")
    @Mapping(source = "story.text", target = "text")
    JpaStory toJpaStory(Story story, JpaAnimal jpaAnimal);
}
