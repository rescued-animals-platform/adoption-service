package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateRequest;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.organization.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = StateRequestMapper.class)
public interface AnimalCreateUpdateRequestMapper {

    AnimalCreateUpdateRequestMapper MAPPER = Mappers.getMapper(AnimalCreateUpdateRequestMapper.class);

    @Mapping(source = "animalCreateUpdateRequest.stateRequest", target = "state")
    @Mapping(source = "animalCreateUpdateRequest.name", target = "name")
    AnimalDto toAnimalDto(AnimalCreateUpdateRequest animalCreateUpdateRequest, Organization organization);
}
