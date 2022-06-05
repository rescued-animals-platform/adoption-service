package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.characteristics.temperaments.TemperamentsResponse;
import ec.animal.adoption.domain.model.characteristics.temperaments.Temperaments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TemperamentsResponseMapper {

    TemperamentsResponseMapper MAPPER = Mappers.getMapper(TemperamentsResponseMapper.class);

    @Mapping(expression = "java( temperaments.getSociability().map(s -> s.name()).orElse(null) )", target = "sociability")
    @Mapping(expression = "java( temperaments.getDocility().map(d -> d.name()).orElse(null) )", target = "docility")
    @Mapping(expression = "java( temperaments.getBalance().map(b -> b.name()).orElse(null) )", target = "balance")
    TemperamentsResponse toTemperamentsResponse(Temperaments temperaments);
}
