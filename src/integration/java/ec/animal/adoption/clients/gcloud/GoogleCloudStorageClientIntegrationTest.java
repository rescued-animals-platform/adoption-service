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

package ec.animal.adoption.clients.gcloud;

import ec.animal.adoption.AdoptionServiceApplication;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = AdoptionServiceApplication.class)
public class GoogleCloudStorageClientIntegrationTest {

    @Autowired
    private GoogleCloudStorageClient googleCloudStorageClient;

    private String mediaPath;

    @Before
    public void setUp() {
        mediaPath = "test/PrimaryImageLarge.jpeg";
    }

    @Test
    public void shouldSaveALargeFile() throws IOException {
        InputStream inputStream = new ClassPathResource("test-image-large.jpeg").getInputStream();
        byte[] fileContent = IOUtils.toByteArray(inputStream);

        String mediaLink = googleCloudStorageClient.storeMedia(mediaPath, fileContent);

        assertNotNull(mediaLink);
    }

    @After
    public void tearDown() {
        boolean mediaDeleted = googleCloudStorageClient.deleteMedia(mediaPath);
        assertTrue(mediaDeleted);
    }
}
