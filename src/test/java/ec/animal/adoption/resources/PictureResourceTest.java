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

package ec.animal.adoption.resources;

import ec.animal.adoption.builders.ImageBuilder;
import ec.animal.adoption.builders.ImagePictureBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.exceptions.InvalidPictureException;
import ec.animal.adoption.services.PictureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PictureResourceTest {

    @Mock
    private MultipartFile largeImageMultipartFile;

    @Mock
    private MultipartFile smallImageMultipartFile;

    @Mock
    private PictureService pictureService;

    private ImagePicture imagePicture;

    @Before
    public void setUp() throws IOException {
        Image largeImage = ImageBuilder.random().build();
        Image smallImage = ImageBuilder.random().build();
        imagePicture = ImagePictureBuilder.random().withLargeImage(largeImage).withSmallImage(smallImage).build();
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(
                randomAlphabetic(10) + "." + largeImage.getExtension()
        );
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(
                randomAlphabetic(10) + "." + smallImage.getExtension()
        );
        when(smallImageMultipartFile.getBytes()).thenReturn(smallImage.getContent());
        when(smallImageMultipartFile.getSize()).thenReturn(smallImage.getSizeInBytes());
    }

    @Test
    public void shouldCreateAPicture() {
        LinkPicture expectedLinkPicture = LinkPictureBuilder.random().withAnimalUuid(imagePicture.getAnimalUuid()).
                withName(imagePicture.getName()).withPictureType(imagePicture.getPictureType()).build();
        when(pictureService.create(imagePicture)).thenReturn(expectedLinkPicture);
        PictureResource pictureResource = new PictureResource(pictureService);

        LinkPicture linkPicture = pictureResource.create(
                imagePicture.getAnimalUuid(),
                imagePicture.getName(),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile
        );

        assertThat(linkPicture, is(expectedLinkPicture));
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionWhenInputStreamCanNotBeAccessedInLargeImage() throws IOException {
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenThrow(IOException.class);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                imagePicture.getAnimalUuid(),
                randomAlphabetic(10),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile
        );
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionWhenInputStreamCanNotBeAccessedInSmallImage() throws IOException {
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(smallImageMultipartFile.getBytes()).thenThrow(IOException.class);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                imagePicture.getAnimalUuid(),
                randomAlphabetic(10),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile
        );
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionWhenMultipartFileIsEmptyInLargeImage() {
        when(largeImageMultipartFile.isEmpty()).thenReturn(true);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                imagePicture.getAnimalUuid(),
                randomAlphabetic(10),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile
        );
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionWhenMultipartFileIsEmptyInSmallImage() {
        when(smallImageMultipartFile.isEmpty()).thenReturn(true);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                imagePicture.getAnimalUuid(),
                randomAlphabetic(10),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile
        );
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionWhenOriginalFilenameIsNullInLargeImage() {
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(null);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                imagePicture.getAnimalUuid(),
                randomAlphabetic(10),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile
        );
    }

    @Test(expected = InvalidPictureException.class)
    public void shouldThrowInvalidPictureExceptionWhenOriginalFilenameIsNullInSmallImage() {
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(null);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                imagePicture.getAnimalUuid(),
                randomAlphabetic(10),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile
        );
    }
}