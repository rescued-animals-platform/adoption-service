package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.characteristics.CharacteristicsRequest;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = TemperamentsRequestMapper.class)
public interface CharacteristicsRequestMapper {

    CharacteristicsRequestMapper MAPPER = Mappers.getMapper(CharacteristicsRequestMapper.class);

    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateWith", ignore = true)
    Characteristics toCharacteristics(CharacteristicsRequest characteristicsRequest);
}
