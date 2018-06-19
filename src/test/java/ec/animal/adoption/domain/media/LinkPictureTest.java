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
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class LinkPictureTest {

    private UUID animalUuid;
    private String name;
    private PictureType pictureType;
    private String largeImageUrl;
    private String smallImageUrl;
    private LinkPicture linkPicture;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        name = randomAlphabetic(10);
        pictureType = getRandomPictureType();
        largeImageUrl = randomAlphabetic(10);
        smallImageUrl = randomAlphabetic(10);
        linkPicture = new LinkPicture(
                animalUuid, name, pictureType, new MediaLink(largeImageUrl), new MediaLink(smallImageUrl)
        );
    }

    @Test
    public void shouldCreateALinkPicture() {
        assertThat(linkPicture.getAnimalUuid(), is(animalUuid));
        assertThat(linkPicture.getName(), is(name));
        assertThat(linkPicture.getPictureType(), is(pictureType));
        assertThat(linkPicture.getLargeImageUrl(), is(largeImageUrl));
        assertThat(linkPicture.getSmallImageUrl(), is(smallImageUrl));
    }

    @Test
    public void shouldReturnTrue() {
        assertThat(linkPicture.hasUrls(), is(true));
    }

    @Test
    public void shouldReturnFalse() {
        assertThat(linkPicture.hasImages(), is(false));
    }

    @Test
    public void shouldBeNull() {
        assertNull(linkPicture.getLargeImagePath());
        assertNull(linkPicture.getLargeImageContent());
        assertNull(linkPicture.getSmallImagePath());
        assertNull(linkPicture.getSmallImageContent());
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(LinkPicture.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedLinkPicture = objectMapper.writeValueAsString(linkPicture);
        LinkPicture deserializedLinkPicture = objectMapper.readValue(serializedLinkPicture, LinkPicture.class);

        assertThat(deserializedLinkPicture, is(linkPicture));
    }
}