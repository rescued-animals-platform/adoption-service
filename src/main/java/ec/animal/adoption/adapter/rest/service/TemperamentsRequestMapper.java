package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.characteristics.temperaments.TemperamentsRequest;
import ec.animal.adoption.domain.model.characteristics.temperaments.Temperaments;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TemperamentsRequestMapper {

    TemperamentsRequestMapper MAPPER = Mappers.getMapper(TemperamentsRequestMapper.class);

    Temperaments toTemperaments(TemperamentsRequest temperamentsRequest);
}
