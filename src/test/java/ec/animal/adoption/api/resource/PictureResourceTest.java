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

package ec.animal.adoption.api.resource;

import ec.animal.adoption.builders.ImageBuilder;
import ec.animal.adoption.builders.ImagePictureBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.domain.exception.InvalidPictureException;
import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureService;
import ec.animal.adoption.domain.media.PictureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PictureResourceTest {

    @Mock
    private MultipartFile largeImageMultipartFile;

    @Mock
    private MultipartFile smallImageMultipartFile;

    @Mock
    private PictureService pictureService;

    private ImagePicture imagePicture;
    private Image largeImage;
    private Image smallImage;
    private PictureResource pictureResource;

    @BeforeEach
    public void setUp() {
        largeImage = ImageBuilder.random().build();
        smallImage = ImageBuilder.random().build();
        imagePicture = ImagePictureBuilder.random().withPictureType(PictureType.PRIMARY)
                                          .withLargeImage(largeImage).withSmallImage(smallImage).build();

        pictureResource = new PictureResource(pictureService);
    }

    @Test
    public void shouldCreateAPicture() throws IOException {
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

        LinkPicture expectedLinkPicture = LinkPictureBuilder.random()
                                                            .withName(imagePicture.getName())
                                                            .withPictureType(imagePicture.getPictureType())
                                                            .build();
        when(pictureService.createPrimaryPicture(imagePicture)).thenReturn(expectedLinkPicture);

        LinkPicture linkPicture = pictureResource.createPrimaryPicture(
                imagePicture.getAnimalUuid(),
                imagePicture.getName(),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile
        );

        assertThat(linkPicture, is(expectedLinkPicture));
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenInputStreamCanNotBeAccessedInLargeImage() throws IOException {
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenThrow(IOException.class);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    imagePicture.getAnimalUuid(),
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenInputStreamCanNotBeAccessedInSmallImage() {
        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    imagePicture.getAnimalUuid(),
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenMultipartFileIsEmptyInLargeImage() {
        when(largeImageMultipartFile.isEmpty()).thenReturn(true);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    imagePicture.getAnimalUuid(),
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenMultipartFileIsEmptyInSmallImage() {
        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    imagePicture.getAnimalUuid(),
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenOriginalFilenameIsNullInLargeImage() {
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(null);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    imagePicture.getAnimalUuid(),
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenOriginalFilenameIsNullInSmallImage() {
        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    imagePicture.getAnimalUuid(),
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile
            );
        });
    }

    @Test
    public void shouldGetPrimaryPictureForAnimal() {
        UUID animalUuid = UUID.randomUUID();
        LinkPicture expectedLinkPicture = mock(LinkPicture.class);
        when(pictureService.getBy(animalUuid)).thenReturn(expectedLinkPicture);

        LinkPicture linkPicture = pictureResource.get(animalUuid);

        assertThat(linkPicture, is(expectedLinkPicture));
    }
}