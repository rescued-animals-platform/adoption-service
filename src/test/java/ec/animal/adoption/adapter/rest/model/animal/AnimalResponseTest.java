package ec.animal.adoption.adapter.rest.model.animal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.adapter.rest.service.AnimalResponseMapper;
import ec.animal.adoption.adapter.rest.service.CharacteristicsResponseMapper;
import ec.animal.adoption.adapter.rest.service.LinkPictureResponseMapper;
import ec.animal.adoption.adapter.rest.service.StateResponseMapper;
import ec.animal.adoption.adapter.rest.service.StoryResponseMapper;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.AnimalFactory;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.media.LinkPictureFactory;
import ec.animal.adoption.domain.model.media.PictureType;
import ec.animal.adoption.domain.model.story.Story;
import ec.animal.adoption.domain.model.story.StoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

class AnimalResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldSerializeAnimalResponse() throws JsonProcessingException {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        Characteristics characteristics = CharacteristicsFactory.random().build();
        Story story = StoryFactory.random().build();
        Animal animal = AnimalFactory.random()
                                     .withIdentifier(UUID.randomUUID())
                                     .withPrimaryLinkPicture(primaryLinkPicture)
                                     .withCharacteristics(characteristics)
                                     .withStory(story)
                                     .withRegistrationDate(LocalDateTime.now())
                                     .build();
        AnimalResponse animalResponse = AnimalResponseMapper.MAPPER.toAnimalResponse(animal);
        String expectedSerializedState = objectMapper.writeValueAsString(StateResponseMapper.MAPPER.toStateResponse(animal.getState()));
        String expectedRegistrationDate = objectMapper.writeValueAsString(animal.getRegistrationDate());
        String expectedPrimaryLinkPicture = objectMapper.writeValueAsString(LinkPictureResponseMapper.MAPPER.toLinkPictureResponse(primaryLinkPicture));
        String expectedCharacteristics = objectMapper.writeValueAsString(CharacteristicsResponseMapper.MAPPER.toCharacteristicsResponse(characteristics));
        String expectedStory = objectMapper.writeValueAsString(StoryResponseMapper.MAPPER.toStoryResponse(story));

        String serializedAnimalResponse = objectMapper.writeValueAsString(animalResponse);

        assertThat(serializedAnimalResponse, containsString(String.format("\"id\":\"%s\"", animal.getIdentifier())));
        assertThat(serializedAnimalResponse, containsString(String.format("\"registrationDate\":%s", expectedRegistrationDate)));
        assertThat(serializedAnimalResponse, containsString(String.format("\"clinicalRecord\":\"%s\"", animal.getClinicalRecord())));
        assertThat(serializedAnimalResponse, containsString(String.format("\"name\":\"%s\"", animal.getName())));
        assertThat(serializedAnimalResponse, containsString(String.format("\"species\":\"%s\"", animal.getSpecies().name())));
        assertThat(serializedAnimalResponse, containsString(String.format("\"estimatedAge\":\"%s\"", animal.getEstimatedAge().name())));
        assertThat(serializedAnimalResponse, containsString(String.format("\"sex\":\"%s\"", animal.getSex().name())));
        assertThat(serializedAnimalResponse, containsString(String.format("\"state\":%s", expectedSerializedState)));
        assertThat(serializedAnimalResponse, containsString(String.format("\"primaryLinkPicture\":%s", expectedPrimaryLinkPicture)));
        assertThat(serializedAnimalResponse, containsString(String.format("\"characteristics\":%s", expectedCharacteristics)));
        assertThat(serializedAnimalResponse, containsString(String.format("\"story\":%s", expectedStory)));
    }
}