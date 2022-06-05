package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.characteristics.CharacteristicsResponse;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = TemperamentsResponseMapper.class)
public interface CharacteristicsResponseMapper {

    CharacteristicsResponseMapper MAPPER = Mappers.getMapper(CharacteristicsResponseMapper.class);

    CharacteristicsResponse toCharacteristicsResponse(Characteristics characteristics);
}
