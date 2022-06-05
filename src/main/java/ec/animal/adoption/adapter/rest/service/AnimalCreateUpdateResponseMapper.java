package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateResponse;
import ec.animal.adoption.domain.model.animal.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = StateResponseMapper.class)
public interface AnimalCreateUpdateResponseMapper {

    AnimalCreateUpdateResponseMapper MAPPER = Mappers.getMapper(AnimalCreateUpdateResponseMapper.class);

    @Mapping(source = "identifier", target = "id")
    AnimalCreateUpdateResponse toAnimalCreateUpdateResponse(Animal animal);
}
