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

package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MediaLinkTest {

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(MediaLink.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeJsonSerializableWithUrl() throws IOException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        String url = randomAlphabetic(10);
        String expectedSerializedMediaLink = "{\"url\":\"" + url + "\"}";
        MediaLink mediaLink = new MediaLink(url);

        String serializedMediaLink = objectMapper.writeValueAsString(mediaLink);

        assertThat(serializedMediaLink, is(expectedSerializedMediaLink));
    }
}