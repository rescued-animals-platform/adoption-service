package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.state.StateResponse;
import ec.animal.adoption.domain.model.state.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StateResponseMapper {

    StateResponseMapper MAPPER = Mappers.getMapper(StateResponseMapper.class);

    @Mapping(expression = "java( state.getAdoptionFormId().orElse(null) )", target = "adoptionFormId")
    @Mapping(expression = "java( state.getNotes().orElse(null) )", target = "notes")
    StateResponse toStateResponse(State state);
}
