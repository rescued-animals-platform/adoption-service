/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.domain.media;

import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalFactory;
import ec.animal.adoption.domain.animal.AnimalRepository;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.exception.InvalidPictureException;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static ec.animal.adoption.domain.media.PictureType.ALTERNATE;
import static ec.animal.adoption.domain.media.PictureType.PRIMARY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PictureServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private MediaRepository mediaRepository;

    private Organization organization;
    private PictureService pictureService;

    @BeforeEach
    public void setUp() {
        organization = OrganizationFactory.random().build();
        pictureService = new PictureService(mediaRepository, animalRepository);
    }

    @Test
    public void shouldCreateAPicture() {
        ArgumentCaptor<Animal> argumentCaptor = ArgumentCaptor.forClass(Animal.class);
        ImagePicture imagePicture = ImagePictureFactory.random().withPictureType(PRIMARY).build();
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withPictureType(PRIMARY).build();
        when(mediaRepository.save(imagePicture, organization)).thenReturn(primaryLinkPicture);
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalFactory.random().withIdentifier(animalId).withOrganization(organization).build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);
        Animal animalWithPrimaryLinkPicture = AnimalFactory.random()
                                                           .withPrimaryLinkPicture(primaryLinkPicture)
                                                           .build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithPrimaryLinkPicture);

        LinkPicture createdPicture = pictureService.createFor(animalId, organization, imagePicture);

        verify(animalRepository).save(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue().getPrimaryLinkPicture().isPresent());
        assertEquals(primaryLinkPicture, argumentCaptor.getValue().getPrimaryLinkPicture().get());
        assertThat(createdPicture, is(primaryLinkPicture));
    }

    @Test
    void shouldThrowEntityAlreadyExistsExceptionWhenThereIsAlreadyAPrimaryLinkPictureForAnimal() {
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalFactory.random()
                                     .withIdentifier(animalId)
                                     .withOrganization(organization)
                                     .withPrimaryLinkPicture(LinkPictureFactory.random()
                                                                               .withPictureType(PRIMARY)
                                                                               .build())
                                     .build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);
        ImagePicture imagePicture = ImagePictureFactory.random().withPictureType(PRIMARY).build();

        assertThrows(EntityAlreadyExistsException.class, () -> {
            pictureService.createFor(animalId, organization, imagePicture);
        });
        verifyNoInteractions(mediaRepository);
    }

    @Test
    public void shouldThrowInvalidPictureExceptionIfPictureTypeIsNotPrimaryOnCreate() {
        ImagePicture imagePicture = ImagePictureFactory.random().withPictureType(ALTERNATE).build();

        assertThrows(InvalidPictureException.class, () -> {
            pictureService.createFor(UUID.randomUUID(), OrganizationFactory.random().build(), imagePicture);
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenImagePictureIsInvalidOnCreate() {
        ImagePicture imagePicture = mock(ImagePicture.class);
        when(imagePicture.getPictureType()).thenReturn(PRIMARY);
        when(imagePicture.isValid()).thenReturn(false);

        assertThrows(InvalidPictureException.class, () -> {
            pictureService.createFor(UUID.randomUUID(), OrganizationFactory.random().build(), imagePicture);
        });
    }

    @Test
    public void shouldUpdatePrimaryLinkPictureWithADifferentOneWhenItAlreadyExistsAndDeleteTheExistingOne() {
        UUID animalId = UUID.randomUUID();
        ImagePicture imagePicture = ImagePictureFactory.random().withPictureType(PRIMARY).build();
        LinkPicture newPrimaryLinkPicture = LinkPictureFactory.random().withPictureType(PRIMARY).build();
        LinkPicture expectedUpdatedPrimaryLinkPicture = LinkPictureFactory.random().withPictureType(PRIMARY).build();
        LinkPicture existingPrimaryLinkPicture = mock(LinkPicture.class);
        when(existingPrimaryLinkPicture.isPrimary()).thenReturn(true);
        when(existingPrimaryLinkPicture.updateWith(newPrimaryLinkPicture))
                .thenReturn(expectedUpdatedPrimaryLinkPicture);
        Animal animal = AnimalFactory.random().withPrimaryLinkPicture(existingPrimaryLinkPicture).build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);
        when(mediaRepository.save(imagePicture, organization)).thenReturn(newPrimaryLinkPicture);
        when(animalRepository.save(any(Animal.class))).thenReturn(
                AnimalFactory.random().withPrimaryLinkPicture(expectedUpdatedPrimaryLinkPicture).build()
        );
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);

        LinkPicture updatedPicture = pictureService.updateFor(animalId, organization, imagePicture);

        verify(animalRepository).save(animalArgumentCaptor.capture());
        verify(mediaRepository).delete(existingPrimaryLinkPicture);
        Animal animalSaved = animalArgumentCaptor.getValue();
        assertTrue(animalSaved.getPrimaryLinkPicture().isPresent());
        assertEquals(expectedUpdatedPrimaryLinkPicture, animalSaved.getPrimaryLinkPicture().get());
        assertEquals(expectedUpdatedPrimaryLinkPicture, updatedPicture);
    }

    @Test
    void shouldCreatePrimaryLinkPictureWhenUpdatingPictureForAnimalThatDoesNotHaveAlreadyAPrimaryLinkPicture() {
        UUID animalId = UUID.randomUUID();
        ImagePicture imagePicture = ImagePictureFactory.random().withPictureType(PRIMARY).build();
        LinkPicture newPrimaryLinkPicture = LinkPictureFactory.random().withPictureType(PRIMARY).build();
        Animal animal = AnimalFactory.random().build();
        when(animalRepository.getBy(animalId, organization)).thenReturn(animal);
        when(mediaRepository.save(imagePicture, organization)).thenReturn(newPrimaryLinkPicture);
        when(animalRepository.save(any(Animal.class))).thenReturn(
                AnimalFactory.random().withPrimaryLinkPicture(newPrimaryLinkPicture).build()
        );
        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);

        LinkPicture updatedPicture = pictureService.updateFor(animalId, organization, imagePicture);

        verify(animalRepository).save(animalArgumentCaptor.capture());
        verify(mediaRepository, never()).delete(any(LinkPicture.class));
        Animal animalSaved = animalArgumentCaptor.getValue();
        assertTrue(animalSaved.getPrimaryLinkPicture().isPresent());
        assertEquals(newPrimaryLinkPicture, animalSaved.getPrimaryLinkPicture().get());
        assertEquals(newPrimaryLinkPicture, updatedPicture);
    }

    @Test
    public void shouldThrowInvalidPictureExceptionIfPictureTypeIsNotPrimaryOnUpdate() {
        ImagePicture imagePicture = ImagePictureFactory.random().withPictureType(ALTERNATE).build();

        assertThrows(InvalidPictureException.class, () -> {
            pictureService.updateFor(UUID.randomUUID(), OrganizationFactory.random().build(), imagePicture);
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenImagePictureIsInvalidOnUpdate() {
        ImagePicture imagePicture = mock(ImagePicture.class);
        when(imagePicture.getPictureType()).thenReturn(PRIMARY);
        when(imagePicture.isValid()).thenReturn(false);

        assertThrows(InvalidPictureException.class, () -> {
            pictureService.updateFor(UUID.randomUUID(), OrganizationFactory.random().build(), imagePicture);
        });
    }

    @Test
    public void shouldGetPrimaryLinkPictureByAnimalId() {
        LinkPicture expectedPrimaryLinkPicture = LinkPictureFactory.random()
                                                                   .withPictureType(PRIMARY).build();
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalFactory.random().withIdentifier(animalId)
                                     .withPrimaryLinkPicture(expectedPrimaryLinkPicture).build();
        when(animalRepository.getBy(animalId)).thenReturn(animal);

        LinkPicture linkPicture = pictureService.getBy(animalId);

        assertThat(linkPicture, is(expectedPrimaryLinkPicture));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenThereIsNoPrimaryLinkPictureForAnimal() {
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalFactory.random().build();
        when(animalRepository.getBy(animalId)).thenReturn(animal);

        assertThrows(EntityNotFoundException.class, () -> {
            pictureService.getBy(animalId);
        });
    }
}