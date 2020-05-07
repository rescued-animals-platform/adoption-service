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

import ec.animal.adoption.api.jwt.AdminTokenUtils;
import ec.animal.adoption.domain.media.ImageBuilder;
import ec.animal.adoption.domain.media.ImagePictureBuilder;
import ec.animal.adoption.domain.media.LinkPictureBuilder;
import ec.animal.adoption.domain.organization.OrganizationBuilder;
import ec.animal.adoption.domain.exception.InvalidPictureException;
import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureService;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @Mock
    private OrganizationService organizationService;

    @Mock
    private AdminTokenUtils adminTokenUtils;

    @Mock
    private Jwt token;

    private UUID animalId;
    private UUID organizationId;
    private Organization organization;
    private ImagePicture imagePicture;
    private Image largeImage;
    private Image smallImage;
    private PictureResource pictureResource;

    @BeforeEach
    public void setUp() {
        animalId = UUID.randomUUID();
        organizationId = UUID.randomUUID();
        organization = OrganizationBuilder.random().withIdentifier(organizationId).build();
        largeImage = ImageBuilder.random().build();
        smallImage = ImageBuilder.random().build();
        imagePicture = ImagePictureBuilder.random().withPictureType(PictureType.PRIMARY)
                                          .withLargeImage(largeImage).withSmallImage(smallImage).build();

        pictureResource = new PictureResource(pictureService, organizationService, adminTokenUtils);
    }

    @Test
    public void shouldCreateAPicture() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
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
        when(pictureService.createFor(animalId, organization, imagePicture)).thenReturn(expectedLinkPicture);

        LinkPicture linkPicture = pictureResource.createPrimaryPicture(
                animalId,
                imagePicture.getName(),
                imagePicture.getPictureType(),
                largeImageMultipartFile,
                smallImageMultipartFile,
                token
        );

        assertThat(linkPicture, is(expectedLinkPicture));
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenInputStreamCanNotBeAccessedInLargeImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.isEmpty()).thenReturn(false);
        when(largeImageMultipartFile.getBytes()).thenThrow(IOException.class);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    animalId,
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile,
                    token
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenInputStreamCanNotBeAccessedInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(smallImageMultipartFile.getBytes()).thenThrow(IOException.class);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    animalId,
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile,
                    token
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenMultipartFileIsEmptyInLargeImage() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.isEmpty()).thenReturn(true);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    animalId,
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile,
                    token
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenMultipartFileIsEmptyInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(smallImageMultipartFile.isEmpty()).thenReturn(true);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    animalId,
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile,
                    token
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenOriginalFilenameIsNullInLargeImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(null);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    animalId,
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile,
                    token
            );
        });
    }

    @Test
    public void shouldThrowInvalidPictureExceptionWhenOriginalFilenameIsNullInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(null);

        assertThrows(InvalidPictureException.class, () -> {
            pictureResource.createPrimaryPicture(
                    animalId,
                    randomAlphabetic(10),
                    imagePicture.getPictureType(),
                    largeImageMultipartFile,
                    smallImageMultipartFile,
                    token
            );
        });
    }

    @Test
    public void shouldGetPrimaryPictureForAnimal() {
        UUID animalId = UUID.randomUUID();
        LinkPicture expectedLinkPicture = mock(LinkPicture.class);
        when(pictureService.getBy(animalId)).thenReturn(expectedLinkPicture);

        LinkPicture linkPicture = pictureResource.get(animalId);

        assertThat(linkPicture, is(expectedLinkPicture));
    }
}