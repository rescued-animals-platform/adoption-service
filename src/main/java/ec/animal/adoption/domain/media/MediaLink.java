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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaLink {

    private final String url;

    @JsonCreator
    public MediaLink(@JsonProperty("url") final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaLink mediaLink = (MediaLink) o;

        return url != null ? url.equals(mediaLink.url) : mediaLink.url == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
