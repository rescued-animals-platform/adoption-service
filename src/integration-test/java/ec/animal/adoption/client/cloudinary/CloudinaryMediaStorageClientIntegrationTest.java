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

package ec.animal.adoption.client.cloudinary;

import com.cloudinary.Cloudinary;
import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CloudinaryMediaStorageClientIntegrationTest {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryMediaStorageClient cloudinaryMediaStorageClient;

    @Test
    public void shouldSaveAnImagePicture() throws IOException {
        byte[] largeImageContent = IOUtils.toByteArray(new ClassPathResource("test-image-large.jpeg").getInputStream());
        byte[] smallImageContent = IOUtils.toByteArray(new ClassPathResource("test-image-small.jpeg").getInputStream());

        ImagePicture imagePicture = new ImagePicture(
                UUID.randomUUID(),
                randomAlphabetic(10),
                PictureType.PRIMARY,
                new Image(".jpeg", largeImageContent, largeImageContent.length),
                new Image(".jpeg", smallImageContent, smallImageContent.length)
        );

        LinkPicture linkPicture = cloudinaryMediaStorageClient.save(imagePicture);

        assertNotNull(linkPicture);
    }

    @After
    public void tearDown() throws Exception {
        cloudinary.api().deleteAllResources(Collections.emptyMap());
        cloudinary.api().deleteFolder("default-organization", Collections.emptyMap());
    }
}
