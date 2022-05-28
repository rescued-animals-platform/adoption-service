package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.adapter.jpa.model.JpaCharacteristics;
import ec.animal.adoption.adapter.jpa.model.JpaFriendlyWith;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.model.characteristics.FriendlyWith;
import ec.animal.adoption.domain.model.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.model.characteristics.Size;
import ec.animal.adoption.domain.model.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.model.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.model.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.model.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.model.characteristics.temperaments.TemperamentsFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class JpaCharacteristicsMapperTest {

    @Test
    void shouldGenerateAnIdWhenCreatingJpaCharacteristicsForCharacteristicsWithNoId() {
        Characteristics characteristics = CharacteristicsFactory.random().withIdentifier(null).build();

        JpaCharacteristics jpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toJpaCharacteristics(
                characteristics, mock(JpaAnimal.class)
        );

        assertNotNull(jpaCharacteristics.getId());
    }

    @Test
    void shouldGenerateARegistrationDateWhenCreatingJpaCharacteristicsForCharacteristicsWithNoRegistrationDate() {
        Characteristics characteristics = CharacteristicsFactory.random().withRegistrationDate(null).build();

        JpaCharacteristics jpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toJpaCharacteristics(
                characteristics, mock(JpaAnimal.class)
        );

        assertNotNull(jpaCharacteristics.getRegistrationDate());
    }

    @Test
    void shouldCreateJpaCharacteristicsFromCharacteristics() {
        Sociability sociability = getRandomSociability();
        Docility docility = getRandomDocility();
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsFactory.random()
                                                       .withSociability(sociability)
                                                       .withDocility(docility)
                                                       .withBalance(balance)
                                                       .build();
        FriendlyWith friendlyWith = getRandomFriendlyWith();
        Characteristics characteristics = CharacteristicsFactory.random()
                                                                .withTemperaments(temperaments)
                                                                .withFriendlyWith(friendlyWith)
                                                                .build();

        JpaCharacteristics jpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toJpaCharacteristics(
                characteristics, mock(JpaAnimal.class)
        );

        assertThat(jpaCharacteristics.getId(), is(characteristics.getIdentifier()));
        assertThat(jpaCharacteristics.getRegistrationDate(), is(characteristics.getRegistrationDate()));
        assertThat(jpaCharacteristics.getSize(), is(characteristics.getSize().name()));
        assertThat(jpaCharacteristics.getSociability(), is(sociability.name()));
        assertThat(jpaCharacteristics.getDocility(), is(docility.name()));
        assertThat(jpaCharacteristics.getBalance(), is(balance.name()));
        assertThat(jpaCharacteristics.getFriendlyWith(), hasSize(1));
        assertThat(jpaCharacteristics.getFriendlyWith(), contains(new JpaFriendlyWith(friendlyWith.name())));
        assertThat(jpaCharacteristics.getPhysicalActivity(), is(characteristics.getPhysicalActivity().name()));
    }

    @Test
    void shouldCreateCharacteristicsFromJpaCharacteristics() {
        UUID id = UUID.randomUUID();
        JpaAnimal jpaAnimal = mock(JpaAnimal.class);
        LocalDateTime registrationDate = LocalDateTime.now();
        Size size = getRandomSize();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        Sociability sociability = getRandomSociability();
        Docility docility = getRandomDocility();
        Balance balance = getRandomBalance();
        FriendlyWith friendlyWith = getRandomFriendlyWith();
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(
                id,
                jpaAnimal,
                registrationDate,
                size.name(),
                physicalActivity.name(),
                sociability.name(),
                docility.name(),
                balance.name(),
                List.of(new JpaFriendlyWith(friendlyWith.name()))
        );

        Characteristics characteristics = JpaCharacteristicsMapper.MAPPER.toCharacteristics(jpaCharacteristics);

        assertThat(characteristics.getIdentifier(), is(id));
        assertThat(characteristics.getRegistrationDate(), is(registrationDate));
        assertThat(characteristics.getSize(), is(size));
        assertTrue(characteristics.getTemperaments().getSociability().isPresent());
        assertThat(characteristics.getTemperaments().getSociability().get(), is(sociability));
        assertTrue(characteristics.getTemperaments().getDocility().isPresent());
        assertThat(characteristics.getTemperaments().getDocility().get(), is(docility));
        assertTrue(characteristics.getTemperaments().getBalance().isPresent());
        assertThat(characteristics.getTemperaments().getBalance().get(), is(balance));
        assertThat(characteristics.getFriendlyWith(), hasSize(1));
        assertThat(characteristics.getFriendlyWith(), contains(friendlyWith));
        assertThat(characteristics.getPhysicalActivity(), is(physicalActivity));
    }

    @Test
    void shouldAcceptNullSociability() {
        Temperaments temperaments = TemperamentsFactory.random().withSociability(null).build();
        Characteristics characteristics = CharacteristicsFactory.random().withTemperaments(temperaments).build();

        JpaCharacteristics jpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toJpaCharacteristics(
                characteristics, mock(JpaAnimal.class)
        );
        Characteristics characteristicsFromJpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toCharacteristics(
                jpaCharacteristics
        );

        assertTrue(characteristicsFromJpaCharacteristics.getTemperaments().getSociability().isEmpty());
    }

    @Test
    void shouldAcceptNullDocility() {
        Temperaments temperaments = TemperamentsFactory.random().withDocility(null).build();
        Characteristics characteristics = CharacteristicsFactory.random().withTemperaments(temperaments).build();

        JpaCharacteristics jpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toJpaCharacteristics(
                characteristics, mock(JpaAnimal.class)
        );
        Characteristics characteristicsFromJpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toCharacteristics(
                jpaCharacteristics
        );

        assertTrue(characteristicsFromJpaCharacteristics.getTemperaments().getDocility().isEmpty());
    }

    @Test
    void shouldAcceptNullBalance() {
        Temperaments temperaments = TemperamentsFactory.random().withBalance(null).build();
        Characteristics characteristics = CharacteristicsFactory.random().withTemperaments(temperaments).build();

        JpaCharacteristics jpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toJpaCharacteristics(
                characteristics, mock(JpaAnimal.class)
        );
        Characteristics characteristicsFromJpaCharacteristics = JpaCharacteristicsMapper.MAPPER.toCharacteristics(
                jpaCharacteristics
        );

        assertTrue(characteristicsFromJpaCharacteristics.getTemperaments().getBalance().isEmpty());
    }
}