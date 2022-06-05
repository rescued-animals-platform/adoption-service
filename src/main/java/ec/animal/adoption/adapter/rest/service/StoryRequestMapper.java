package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.story.StoryRequest;
import ec.animal.adoption.domain.model.story.Story;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoryRequestMapper {

    StoryRequestMapper MAPPER = Mappers.getMapper(StoryRequestMapper.class);

    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateWith", ignore = true)
    Story toStory(StoryRequest storyRequest);
}
