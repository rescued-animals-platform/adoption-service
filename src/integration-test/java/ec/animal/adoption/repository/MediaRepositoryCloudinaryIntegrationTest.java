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

package ec.animal.adoption.repository;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.exceptions.NotFound;
import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MediaRepositoryCloudinaryIntegrationTest {

    private static final String JPEG = ".jpeg";

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private MediaRepositoryCloudinary mediaRepositoryCloudinary;

    private ImagePicture imagePicture;
    private String expectedName;
    private Organization organization;

    @BeforeEach
    void setUp() throws IOException {
        byte[] largeImageContent = IOUtils.toByteArray(new ClassPathResource("test-image-large.jpeg").getInputStream());
        byte[] smallImageContent = IOUtils.toByteArray(new ClassPathResource("test-image-small.jpeg").getInputStream());
        expectedName = randomAlphabetic(10);
        imagePicture = new ImagePicture(
                expectedName,
                PictureType.PRIMARY,
                new Image(JPEG, largeImageContent, largeImageContent.length),
                new Image(JPEG, smallImageContent, smallImageContent.length)
        );
        organization = OrganizationFactory.randomDefaultOrganization().build();
    }

    @Test
    public void shouldSaveAnImagePicture() {
        LinkPicture linkPicture = mediaRepositoryCloudinary.save(imagePicture, organization);

        assertEquals(expectedName, linkPicture.getName());
        assertEquals(PictureType.PRIMARY, linkPicture.getPictureType());
        assertNotNull(linkPicture.getLargeImagePublicId());
        assertNotNull(linkPicture.getLargeImageUrl());
        assertNotNull(linkPicture.getSmallImagePublicId());
        assertNotNull(linkPicture.getSmallImageUrl());
    }

    @Test
    public void shouldDeleteAnExistingLinkPicture() {
        LinkPicture existingLinkPicture = mediaRepositoryCloudinary.save(imagePicture, organization);

        assertDoesNotThrow(() -> {
            mediaRepositoryCloudinary.delete(existingLinkPicture);
        });

        assertThrows(NotFound.class, () -> {
            cloudinary.api().resource(existingLinkPicture.getLargeImagePublicId(), Collections.emptyMap());
        });
        assertThrows(NotFound.class, () -> {
            cloudinary.api().resource(existingLinkPicture.getSmallImagePublicId(), Collections.emptyMap());
        });
    }

    @AfterEach
    public void tearDown() throws Exception {
        cloudinary.api().deleteAllResources(Collections.emptyMap());
    }
}
