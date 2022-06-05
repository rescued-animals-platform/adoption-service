package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaFriendlyWith;
import ec.animal.adoption.domain.model.characteristics.FriendlyWith;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface JpaFriendlyWithMapper {

    default Set<FriendlyWith> toFriendlyWithCollection(List<JpaFriendlyWith> jpaFriendlyWith) {
        return jpaFriendlyWith.stream()
                              .map(JpaFriendlyWith::getFriendlyWith)
                              .map(FriendlyWith::valueOf)
                              .collect(Collectors.toSet());
    }

    default List<JpaFriendlyWith> toJpaFriendlyWithCollection(Set<FriendlyWith> friendlyWith) {
        return friendlyWith.stream().map(Enum::name).map(JpaFriendlyWith::new).toList();
    }
}
