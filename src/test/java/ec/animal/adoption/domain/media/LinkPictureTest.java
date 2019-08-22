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
import ec.animal.adoption.builders.LinkPictureBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LinkPictureTest {

    @Test
    public void shouldCreateALinkPicture() {
        String name = randomAlphabetic(10);
        PictureType pictureType = TestUtils.getRandomPictureType();
        String largeImageUrl = randomAlphabetic(30);
        String smallImageUrl = randomAlphabetic(30);
        LinkPicture linkPicture = LinkPictureBuilder.random().withName(name).withPictureType(pictureType)
                .withLargeImageMediaLink(new MediaLink(largeImageUrl))
                .withSmallImageMediaLink(new MediaLink(smallImageUrl)).build();

        assertThat(linkPicture.getName(), is(name));
        assertThat(linkPicture.getPictureType(), is(pictureType));
        assertThat(linkPicture.getLargeImageUrl(), is(largeImageUrl));
        assertThat(linkPicture.getSmallImageUrl(), is(smallImageUrl));
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(LinkPicture.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        LinkPicture linkPicture = LinkPictureBuilder.random().build();
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        String serializedLinkPicture = objectMapper.writeValueAsString(linkPicture);
        LinkPicture deserializedLinkPicture = objectMapper.readValue(serializedLinkPicture, LinkPicture.class);

        assertThat(deserializedLinkPicture, is(linkPicture));
    }
}