package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.story.StoryResponse;
import ec.animal.adoption.domain.model.story.Story;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoryResponseMapper {

    StoryResponseMapper MAPPER = Mappers.getMapper(StoryResponseMapper.class);

    StoryResponse toStoryResponse(Story story);
}
