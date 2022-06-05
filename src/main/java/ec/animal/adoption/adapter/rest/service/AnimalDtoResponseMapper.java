package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.animal.dto.AnimalDtoResponse;
import ec.animal.adoption.domain.model.animal.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnimalDtoResponseMapper {

    AnimalDtoResponseMapper MAPPER = Mappers.getMapper(AnimalDtoResponseMapper.class);

    @Mapping(source = "identifier", target = "id")
    @Mapping(expression = "java( animal.getPrimaryLinkPicture().map(l -> l.getSmallImageUrl()).orElse(null) )", target = "smallPrimaryPictureUrl")
    AnimalDtoResponse toAnimalDtoResponse(Animal animal);
}
