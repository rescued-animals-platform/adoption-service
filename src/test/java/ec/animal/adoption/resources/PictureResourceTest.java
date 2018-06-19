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

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.services.PictureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PictureResourceTest {

    @Mock
    private MultipartFile largeImageMultipartFile;

    @Mock
    private MultipartFile smallImageMultipartFile;

    @Mock
    private PictureService pictureService;

    private UUID animalUuid;
    private PictureType pictureType;
    private String extension;
    private long sizeInBytes;
    private byte[] content;

    @Before
    public void setUp() throws IOException {
        animalUuid = UUID.randomUUID();
        pictureType = TestUtils.getRandomPictureType();
        extension = "ext";
        String filename = randomAlphabetic(10) + "." + extension;
        content = new byte[]{};
        sizeInBytes = new Random().nextLong();
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(filename);
        when(largeImageMultipartFile.getBytes()).thenReturn(content);
        when(largeImageMultipartFile.getSize()).thenReturn(sizeInBytes);
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(filename);
        when(smallImageMultipartFile.getBytes()).thenReturn(content);
        when(smallImageMultipartFile.getSize()).thenReturn(sizeInBytes);
    }

    @Test
    public void shouldCreateAPicture() {
        String name = randomAlphabetic(10);
        LinkPicture expectedLinkPicture = new LinkPicture(
                animalUuid, name, pictureType, mock(MediaLink.class), mock(MediaLink.class)
        );
        ImagePicture imagePicture = new ImagePicture(
                animalUuid,
                name,
                pictureType,
                new Image(extension, content, sizeInBytes),
                new Image(extension, content, sizeInBytes)
        );
        when(pictureService.create(imagePicture)).thenReturn(expectedLinkPicture);
        PictureResource pictureResource = new PictureResource(pictureService);

        Picture picture = pictureResource.create(
                animalUuid, name, pictureType, largeImageMultipartFile, smallImageMultipartFile
        );

        assertThat(picture, is(expectedLinkPicture));
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenInputStreamCanNotBeAccessedInLargeImage() throws IOException {
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenThrow(IOException.class);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                animalUuid, randomAlphabetic(10), pictureType, largeImageMultipartFile, smallImageMultipartFile
        );
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenInputStreamCanNotBeAccessedInSmallImage() throws IOException {
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(smallImageMultipartFile.getBytes()).thenThrow(IOException.class);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                animalUuid, randomAlphabetic(10), pictureType, largeImageMultipartFile, smallImageMultipartFile
        );
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenMultipartFileIsEmptyInLargeImage() {
        when(largeImageMultipartFile.isEmpty()).thenReturn(true);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                animalUuid, randomAlphabetic(10), pictureType, largeImageMultipartFile, smallImageMultipartFile
        );
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenMultipartFileIsEmptyInSmallImage() {
        when(smallImageMultipartFile.isEmpty()).thenReturn(true);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                animalUuid, randomAlphabetic(10), pictureType, largeImageMultipartFile, smallImageMultipartFile
        );
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenOriginalFilenameIsNullInLargeImage() {
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(null);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                animalUuid, randomAlphabetic(10), pictureType, largeImageMultipartFile, smallImageMultipartFile
        );
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenOriginalFilenameIsNullInSmallImage() {
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(null);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(
                animalUuid, randomAlphabetic(10), pictureType, largeImageMultipartFile, smallImageMultipartFile
        );
    }
}