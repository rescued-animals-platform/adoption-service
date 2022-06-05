package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.adapter.jpa.model.JpaCharacteristics;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.model.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.model.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.model.characteristics.temperaments.Temperaments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(uses = {JpaFriendlyWithMapper.class})
public interface JpaCharacteristicsMapper {

    JpaCharacteristicsMapper MAPPER = Mappers.getMapper(JpaCharacteristicsMapper.class);

    @Mapping(source = "id", target = "identifier")
    @Mapping(source = "sociability", target = "temperaments.sociability")
    @Mapping(source = "docility", target = "temperaments.docility")
    @Mapping(source = "balance", target = "temperaments.balance")
    @Mapping(target = "updateWith", ignore = true)
    Characteristics toCharacteristics(JpaCharacteristics jpaCharacteristics);

    @Mapping(source = "characteristics.identifier", target = "id", defaultExpression = "java( UUID.randomUUID() )")
    @Mapping(source = "characteristics.registrationDate", target = "registrationDate", defaultExpression = "java( LocalDateTime.now() )")
    @Mapping(source = "characteristics.temperaments", target = "sociability", qualifiedByName = "toStringSociability")
    @Mapping(source = "characteristics.temperaments", target = "docility", qualifiedByName = "toStringDocility")
    @Mapping(source = "characteristics.temperaments", target = "balance", qualifiedByName = "toStringBalance")
    JpaCharacteristics toJpaCharacteristics(Characteristics characteristics, JpaAnimal jpaAnimal);

    @Named("toStringSociability")
    default String toStringSociability(Temperaments temperaments) {
        return Optional.ofNullable(temperaments)
                       .flatMap(Temperaments::getSociability)
                       .map(Sociability::name)
                       .orElse(null);
    }

    @Named("toStringDocility")
    default String toStringDocility(Temperaments temperaments) {
        return Optional.ofNullable(temperaments)
                       .flatMap(Temperaments::getDocility)
                       .map(Docility::name)
                       .orElse(null);
    }

    @Named("toStringBalance")
    default String toStringBalance(Temperaments temperaments) {
        return Optional.ofNullable(temperaments)
                       .flatMap(Temperaments::getBalance)
                       .map(Balance::name)
                       .orElse(null);
    }
}
