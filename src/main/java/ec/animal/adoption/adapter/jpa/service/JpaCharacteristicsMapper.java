package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.adapter.jpa.model.JpaCharacteristics;
import ec.animal.adoption.adapter.jpa.model.JpaFriendlyWith;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.characteristics.FriendlyWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface JpaCharacteristicsMapper {

    JpaCharacteristicsMapper MAPPER = Mappers.getMapper(JpaCharacteristicsMapper.class);

    @Mapping(source = "id", target = "identifier")
    @Mapping(source = "sociability", target = "temperaments.sociability")
    @Mapping(source = "docility", target = "temperaments.docility")
    @Mapping(source = "balance", target = "temperaments.balance")
    @Mapping(target = "updateWith", ignore = true)
    Characteristics toCharacteristics(JpaCharacteristics jpaCharacteristics);

    default Set<FriendlyWith> toFriendlyWithCollection(List<JpaFriendlyWith> jpaFriendlyWith) {
        return jpaFriendlyWith.stream()
                              .map(JpaFriendlyWith::getFriendlyWith)
                              .map(FriendlyWith::valueOf)
                              .collect(Collectors.toSet());
    }

    @Mapping(source = "characteristics.identifier", target = "id", defaultExpression = "java( UUID.randomUUID() )")
    @Mapping(source = "characteristics.registrationDate", target = "registrationDate", defaultExpression = "java( LocalDateTime.now() )")
    @Mapping(source = "characteristics.temperaments.sociability", target = "sociability", qualifiedByName = "mapEnum")
    @Mapping(source = "characteristics.temperaments.docility", target = "docility", qualifiedByName = "mapEnum")
    @Mapping(source = "characteristics.temperaments.balance", target = "balance", qualifiedByName = "mapEnum")
    JpaCharacteristics toJpaCharacteristics(Characteristics characteristics, JpaAnimal jpaAnimal);

    default List<JpaFriendlyWith> toJpaFriendlyWithCollection(Set<FriendlyWith> friendlyWith) {
        return friendlyWith.stream().map(Enum::name).map(JpaFriendlyWith::new).toList();
    }

    @Named("mapEnum")
    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "rawtypes"})
    default String map(Optional<? extends Enum> anEnum) {
        return anEnum.map(Enum::name).orElse(null);
    }
}
