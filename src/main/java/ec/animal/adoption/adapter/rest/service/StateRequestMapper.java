package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.state.StateRequest;
import ec.animal.adoption.domain.model.state.State;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StateRequestMapper {

    StateRequestMapper MAPPER = Mappers.getMapper(StateRequestMapper.class);

    default State toState(StateRequest stateRequest) {
        if (stateRequest == null) {
            return null;
        }

        return switch (stateRequest.name()) {
            case LOOKING_FOR_HUMAN -> State.lookingForHuman();
            case ADOPTED -> State.adopted(stateRequest.adoptionFormId());
            case UNAVAILABLE -> State.unavailable(stateRequest.notes());
        };
    }
}
