package ec.animal.adoption.domain.animal;

import ec.animal.adoption.domain.animal.characteristics.Characteristics;
import ec.animal.adoption.domain.animal.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.animal.media.LinkPicture;
import ec.animal.adoption.domain.animal.media.LinkPictureFactory;
import ec.animal.adoption.domain.animal.media.PictureType;
import ec.animal.adoption.domain.animal.story.Story;
import ec.animal.adoption.domain.animal.story.StoryFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnimalBuilderTest {

    @Test
    void shouldSetPrimaryLinkPicture() {
        Animal animal = AnimalFactory.random().build();
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        Animal animalWithPrimaryLinkPicture = AnimalBuilder.copyOf(animal).with(primaryLinkPicture).build();

        assertTrue(animalWithPrimaryLinkPicture.getPrimaryLinkPicture().isPresent());
        assertEquals(primaryLinkPicture, animalWithPrimaryLinkPicture.getPrimaryLinkPicture().get());
    }

    @Test
    void shouldUpdatePrimaryLinkPictureWhenItAlreadyHasOne() {
        LinkPicture existingPrimaryLinkPicture = mock(LinkPicture.class);
        when(existingPrimaryLinkPicture.isPrimary()).thenReturn(true);
        LinkPicture newPrimaryLinkPicture = mock(LinkPicture.class);
        LinkPicture updatedPrimaryLinkPicture = mock(LinkPicture.class);
        when(updatedPrimaryLinkPicture.isPrimary()).thenReturn(true);
        when(existingPrimaryLinkPicture.updateWith(newPrimaryLinkPicture)).thenReturn(updatedPrimaryLinkPicture);
        Animal animal = AnimalFactory.random().withPrimaryLinkPicture(existingPrimaryLinkPicture).build();

        Animal animalWithPrimaryLinkPicture = AnimalBuilder.copyOf(animal).with(newPrimaryLinkPicture).build();

        assertTrue(animalWithPrimaryLinkPicture.getPrimaryLinkPicture().isPresent());
        assertEquals(updatedPrimaryLinkPicture, animalWithPrimaryLinkPicture.getPrimaryLinkPicture().get());
    }

    @Test
    void shouldSetStory() {
        Animal animal = AnimalFactory.random().build();
        Story story = StoryFactory.random().build();

        Animal animalWithStory = AnimalBuilder.copyOf(animal).with(story).build();

        assertTrue(animalWithStory.getStory().isPresent());
        assertEquals(story, animalWithStory.getStory().get());
    }

    @Test
    void shouldUpdateStoryWhenItAlreadyHasOne() {
        Story existingStory = mock(Story.class);
        Story newStory = mock(Story.class);
        Story updatedStory = mock(Story.class);
        when(existingStory.updateWith(newStory)).thenReturn(updatedStory);
        Animal animal = AnimalFactory.random().withStory(existingStory).build();

        Animal animalWithStory = AnimalBuilder.copyOf(animal).with(newStory).build();

        assertTrue(animalWithStory.getStory().isPresent());
        assertEquals(updatedStory, animalWithStory.getStory().get());
    }

    @Test
    void shouldSetCharacteristics() {
        Animal animal = AnimalFactory.random().build();
        Characteristics characteristics = CharacteristicsFactory.random().build();
        Animal animalWithCharacteristics = AnimalBuilder.copyOf(animal).with(characteristics).build();

        assertTrue(animalWithCharacteristics.getCharacteristics().isPresent());
        assertEquals(characteristics, animalWithCharacteristics.getCharacteristics().get());
    }

    @Test
    void shouldUpdateCharacteristicsWhenItAlreadyHasOne() {
        Characteristics existingCharacteristics = mock(Characteristics.class);
        Characteristics newCharacteristics = mock(Characteristics.class);
        Characteristics updatedCharacteristics = mock(Characteristics.class);
        when(existingCharacteristics.updateWith(newCharacteristics)).thenReturn(updatedCharacteristics);
        Animal animal = AnimalFactory.random().withCharacteristics(existingCharacteristics).build();

        Animal animalWithCharacteristics = AnimalBuilder.copyOf(animal).with(newCharacteristics).build();

        assertTrue(animalWithCharacteristics.getCharacteristics().isPresent());
        assertEquals(updatedCharacteristics, animalWithCharacteristics.getCharacteristics().get());
    }
}