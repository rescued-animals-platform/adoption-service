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

package ec.animal.adoption.service;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.ImagePictureBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.client.MediaStorageClient;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.exception.EntityNotFoundException;
import ec.animal.adoption.exception.InvalidPictureException;
import ec.animal.adoption.repository.AnimalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PictureServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private MediaStorageClient mediaStorageClient;

    private PictureService pictureService;


    @Before
    public void setUp() {
        pictureService = new PictureService(mediaStorageClient, animalRepository);
    }

    @Test
    public void shouldCreateAPicture() {
        ArgumentCaptor<Animal> argumentCaptor = ArgumentCaptor.forClass(Animal.class);
        ImagePicture imagePicture = ImagePictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        when(mediaStorageClient.save(imagePicture)).thenReturn(primaryLinkPicture);
        Animal animal = AnimalBuilder.random().withUuid(imagePicture.getAnimalUuid())
                .withRegistrationDate(LocalDateTime.now()).build();
        when(animalRepository.getBy(imagePicture.getAnimalUuid())).thenReturn(animal);
        Animal animalWithPrimaryLinkPicture = AnimalBuilder.random().withPrimaryLinkPicture(primaryLinkPicture)
                .withState(animal.getState()).withClinicalRecord(animal.getClinicalRecord()).withName(animal.getName())
                .withEstimatedAge(animal.getEstimatedAge()).withSex(animal.getSex()).withSpecies(animal.getSpecies())
                .withRegistrationDate(animal.getRegistrationDate()).withUuid(animal.getUuid()).build();
        when(animalRepository.save(any(Animal.class))).thenReturn(animalWithPrimaryLinkPicture);

        LinkPicture createdPicture = pictureService.createPrimaryPicture(imagePicture);

        verify(animalRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getPrimaryLinkPicture(), is(primaryLinkPicture));
        assertThat(createdPicture, is(primaryLinkPicture));
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionIfPictureTypeIsNotPrimary() {
        ImagePicture imagePicture = ImagePictureBuilder.random().withPictureType(PictureType.ALTERNATE).build();

        pictureService.createPrimaryPicture(imagePicture);
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionWhenImagePictureIsInvalid() {
        ImagePicture imagePicture = mock(ImagePicture.class);
        when(imagePicture.getPictureType()).thenReturn(PictureType.PRIMARY);
        when(imagePicture.isValid()).thenReturn(false);

        pictureService.createPrimaryPicture(imagePicture);
    }

    @Test
    public void shouldGetPrimaryLinkPictureByAnimalUuid() {
        LinkPicture expectedPrimaryLinkPicture = LinkPictureBuilder.random()
                .withPictureType(PictureType.PRIMARY).build();
        UUID animalUuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().withUuid(animalUuid)
                .withPrimaryLinkPicture(expectedPrimaryLinkPicture).build();
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);

        LinkPicture linkPicture = pictureService.getBy(animalUuid);

        assertThat(linkPicture, is(expectedPrimaryLinkPicture));
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenThereIsNoPrimaryLinkPictureForAnimal() {
        UUID animalUuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().build();
        when(animalRepository.getBy(animalUuid)).thenReturn(animal);

        pictureService.getBy(animalUuid);
    }
}