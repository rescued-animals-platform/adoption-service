package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.characteristics.CharacteristicsResponse;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.characteristics.FriendlyWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = TemperamentsResponseMapper.class)
public interface CharacteristicsResponseMapper {

    CharacteristicsResponseMapper MAPPER = Mappers.getMapper(CharacteristicsResponseMapper.class);

    @Mapping(target = "friendlyWith", qualifiedByName = "toSetOfStrings")
    CharacteristicsResponse toCharacteristicsResponse(Characteristics characteristics);

    @Named("toSetOfStrings")
    default Set<String> toSetOfStrings(Set<FriendlyWith> friendlyWith) {
        return friendlyWith.stream().map(FriendlyWith::name).collect(Collectors.toSet());
    }
}
