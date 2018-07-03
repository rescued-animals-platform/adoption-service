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

package ec.animal.adoption.services;

import ec.animal.adoption.builders.ImagePictureBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.clients.MediaStorageClient;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.exceptions.InvalidPictureException;
import ec.animal.adoption.repositories.LinkPictureRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PictureServiceTest {

    @Mock
    private LinkPictureRepository linkPictureRepository;

    @Mock
    private MediaStorageClient mediaStorageClient;

    private PictureService pictureService;


    @Before
    public void setUp() {
        pictureService = new PictureService(mediaStorageClient, linkPictureRepository);
    }

    @Test
    public void shouldCreateAPicture() {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        LinkPicture linkPicture = LinkPictureBuilder.random().build();
        when(mediaStorageClient.save(imagePicture)).thenReturn(linkPicture);
        when(linkPictureRepository.save(linkPicture)).thenReturn(linkPicture);

        LinkPicture createdPicture = pictureService.create(imagePicture);

        assertThat(createdPicture, is(linkPicture));
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionWhenImagePictureIsInvalid() {
        ImagePicture imagePicture = mock(ImagePicture.class);
        when(imagePicture.isValid()).thenReturn(false);

        pictureService.create(imagePicture);
    }

    @Test
    public void shouldGetPrimaryPictureByAnimalUuid() {
        LinkPicture expectedLinkPicture = mock(LinkPicture.class);
        UUID animalUuid = UUID.randomUUID();
        when(linkPictureRepository.getBy(animalUuid)).thenReturn(expectedLinkPicture);

        LinkPicture linkPicture = pictureService.getBy(animalUuid);

        assertThat(linkPicture, is(expectedLinkPicture));
    }
}