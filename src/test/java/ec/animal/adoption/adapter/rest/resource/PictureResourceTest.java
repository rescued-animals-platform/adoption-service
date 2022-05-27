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

package ec.animal.adoption.adapter.rest.resource;

import ec.animal.adoption.adapter.rest.jwt.AdminTokenUtils;
import ec.animal.adoption.adapter.rest.model.media.LinkPictureResponse;
import ec.animal.adoption.application.OrganizationService;
import ec.animal.adoption.application.PictureService;
import ec.animal.adoption.domain.model.exception.InvalidPictureException;
import ec.animal.adoption.domain.model.media.*;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.organization.OrganizationFactory;
import org.assertj.core.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PictureResourceTest {

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
        organization = OrganizationFactory.random().withIdentifier(organizationId).build();
        largeImage = ImageFactory.random().build();
        smallImage = ImageFactory.random().build();
        imagePicture = ImagePictureFactory.random().withPictureType(PictureType.PRIMARY)
                                          .withLargeImage(largeImage).withSmallImage(smallImage).build();

        pictureResource = new PictureResource(pictureService, organizationService, adminTokenUtils);
    }

    @Test
    void shouldCreateAPicture() throws IOException {
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
        LinkPicture expectedLinkPicture = LinkPictureFactory.random()
                                                            .withName(imagePicture.getName())
                                                            .withPictureType(imagePicture.getPictureType())
                                                            .build();
        when(pictureService.createFor(animalId, organization, imagePicture)).thenReturn(expectedLinkPicture);
        LinkPictureResponse expectedLinkPictureResponse = LinkPictureResponse.from(expectedLinkPicture);

        LinkPictureResponse linkPictureResponse = pictureResource.createPrimaryPicture(animalId,
                                                                                       imagePicture.getName(),
                                                                                       imagePicture.getPictureType(),
                                                                                       largeImageMultipartFile,
                                                                                       smallImageMultipartFile,
                                                                                       token);

        Assertions.assertThat(linkPictureResponse).usingRecursiveComparison().isEqualTo(expectedLinkPictureResponse);
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenCreatingAPrimaryPictureAndInputStreamCanNotBeAccessedInLargeImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.isEmpty()).thenReturn(false);
        when(largeImageMultipartFile.getBytes()).thenThrow(IOException.class);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.createPrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenCreatingAPrimaryPictureAndInputStreamCanNotBeAccessedInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(smallImageMultipartFile.getBytes()).thenThrow(IOException.class);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.createPrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenCreatingAPrimaryPictureAndMultipartFileIsEmptyInLargeImage() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.isEmpty()).thenReturn(true);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.createPrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenCreatingAPrimaryPictureAndMultipartFileIsEmptyInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(smallImageMultipartFile.isEmpty()).thenReturn(true);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.createPrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenCreatingAPrimaryPictureAndOriginalFilenameIsNullInLargeImage() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(null);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.createPrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenCreatingAPrimaryPictureAndOriginalFilenameIsNullInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(null);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.createPrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldUpdateAPicture() throws IOException {
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
        LinkPicture expectedLinkPicture = LinkPictureFactory.random()
                                                            .withName(imagePicture.getName())
                                                            .withPictureType(imagePicture.getPictureType())
                                                            .build();
        when(pictureService.updateFor(animalId, organization, imagePicture)).thenReturn(expectedLinkPicture);
        LinkPictureResponse expectedLinkPictureResponse = LinkPictureResponse.from(expectedLinkPicture);

        LinkPictureResponse linkPictureResponse = pictureResource.updatePrimaryPicture(animalId,
                                                                                       imagePicture.getName(),
                                                                                       imagePicture.getPictureType(),
                                                                                       largeImageMultipartFile,
                                                                                       smallImageMultipartFile,
                                                                                       token);

        Assertions.assertThat(linkPictureResponse).usingRecursiveComparison().isEqualTo(expectedLinkPictureResponse);
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenUpdatingAPrimaryPictureAndInputStreamCanNotBeAccessedInLargeImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.isEmpty()).thenReturn(false);
        when(largeImageMultipartFile.getBytes()).thenThrow(IOException.class);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.updatePrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenUpdatingAPrimaryPictureAndInputStreamCanNotBeAccessedInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(smallImageMultipartFile.getBytes()).thenThrow(IOException.class);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.updatePrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenUpdatingAPrimaryPictureAndMultipartFileIsEmptyInLargeImage() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.isEmpty()).thenReturn(true);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.updatePrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenUpdatingAPrimaryPictureAndMultipartFileIsEmptyInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(smallImageMultipartFile.isEmpty()).thenReturn(true);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.updatePrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenUpdatingAPrimaryPictureAndOriginalFilenameIsNullInLargeImage() {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(null);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.updatePrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldThrowInvalidPictureExceptionWhenUpdatingAPrimaryPictureAndOriginalFilenameIsNullInSmallImage() throws IOException {
        when(adminTokenUtils.extractOrganizationIdFrom(token)).thenReturn(organizationId);
        when(organizationService.getBy(organizationId)).thenReturn(organization);
        when(largeImageMultipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(largeImageMultipartFile.getBytes()).thenReturn(largeImage.getContent());
        when(largeImageMultipartFile.getSize()).thenReturn(largeImage.getSizeInBytes());
        when(smallImageMultipartFile.getOriginalFilename()).thenReturn(null);
        var name = randomAlphabetic(10);
        var pictureType = imagePicture.getPictureType();

        assertThrows(InvalidPictureException.class, () -> pictureResource.updatePrimaryPicture(animalId,
                                                                                               name,
                                                                                               pictureType,
                                                                                               largeImageMultipartFile,
                                                                                               smallImageMultipartFile,
                                                                                               token));
    }

    @Test
    void shouldGetPrimaryPictureForAnimal() {
        UUID animalId = UUID.randomUUID();
        LinkPicture linkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        when(pictureService.getBy(animalId)).thenReturn(linkPicture);
        LinkPictureResponse expectedLinkPictureResponse = LinkPictureResponse.from(linkPicture);

        LinkPictureResponse linkPictureResponse = pictureResource.get(animalId);

        Assertions.assertThat(linkPictureResponse).usingRecursiveComparison().isEqualTo(expectedLinkPictureResponse);
    }
}