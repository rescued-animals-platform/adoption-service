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

package ec.animal.adoption.api.model.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.media.LinkPicture;

@SuppressWarnings("PMD")
public class LinkPictureResponse {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("pictureType")
    private final String pictureType;

    @JsonProperty("largeImageMediaLink")
    private final MediaLinkResponse largeImageMediaLink;

    @JsonProperty("smallImageMediaLink")
    private final MediaLinkResponse smallImageMediaLink;

    @JsonCreator
    private LinkPictureResponse(@JsonProperty("name") final String name,
                                @JsonProperty("pictureType") final String pictureType,
                                @JsonProperty("largeImageMediaLink") final MediaLinkResponse largeImageMediaLink,
                                @JsonProperty("smallImageMediaLink") final MediaLinkResponse smallImageMediaLink) {
        super();
        this.name = name;
        this.pictureType = pictureType;
        this.largeImageMediaLink = largeImageMediaLink;
        this.smallImageMediaLink = smallImageMediaLink;
    }

    public static LinkPictureResponse from(final LinkPicture linkPicture) {
        return new LinkPictureResponse(linkPicture.getName(),
                                       linkPicture.getPictureType().name(),
                                       MediaLinkResponse.from(linkPicture.getLargeImageUrl()),
                                       MediaLinkResponse.from(linkPicture.getSmallImageUrl()));
    }
}
