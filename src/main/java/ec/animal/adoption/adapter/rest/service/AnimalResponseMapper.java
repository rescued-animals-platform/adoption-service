package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.animal.AnimalResponse;
import ec.animal.adoption.adapter.rest.model.characteristics.CharacteristicsResponse;
import ec.animal.adoption.adapter.rest.model.media.LinkPictureResponse;
import ec.animal.adoption.adapter.rest.model.story.StoryResponse;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.story.Story;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(uses = StateResponseMapper.class)
public interface AnimalResponseMapper {

    AnimalResponseMapper MAPPER = Mappers.getMapper(AnimalResponseMapper.class);

    @Mapping(source = "identifier", target = "id")
    @Mapping(target = "primaryLinkPicture", qualifiedByName = "toLinkPictureResponse")
    @Mapping(target = "characteristics", qualifiedByName = "toCharacteristicsResponse")
    @Mapping(target = "story", qualifiedByName = "toStoryResponse")
    AnimalResponse toAnimalResponse(Animal animal);

    @Named("toLinkPictureResponse")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    default LinkPictureResponse toLinkPictureResponse(Optional<LinkPicture> linkPicture) {
        return linkPicture.map(LinkPictureResponseMapper.MAPPER::toLinkPictureResponse).orElse(null);
    }

    @Named("toCharacteristicsResponse")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    default CharacteristicsResponse toCharacteristicsResponse(Optional<Characteristics> characteristics) {
        return characteristics.map(CharacteristicsResponseMapper.MAPPER::toCharacteristicsResponse).orElse(null);
    }

    @Named("toStoryResponse")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    default StoryResponse toStoryResponse(Optional<Story> story) {
        return story.map(StoryResponseMapper.MAPPER::toStoryResponse).orElse(null);
    }
}
