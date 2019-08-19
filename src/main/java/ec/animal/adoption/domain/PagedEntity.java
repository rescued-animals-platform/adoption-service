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

package ec.animal.adoption.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@JsonIgnoreProperties({"pageable"})
public class PagedEntity<T> extends PageImpl<T> {

    private transient static final long serialVersionUID = -721000112304438810L;

    @SuppressWarnings({"PMD.ExcessiveParameterList", "PMD.UnusedFormalParameter"})
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PagedEntity(
            final @JsonProperty("content") List<T> content,
            final @JsonProperty("number") int number,
            final @JsonProperty("size") int size,
            final @JsonProperty("totalElements") long totalElements,
            final @JsonProperty("last") boolean last,
            final @JsonProperty("totalPages") int totalPages,
            final @JsonProperty("sort") JsonNode sort,
            final @JsonProperty("first") boolean first,
            final @JsonProperty("numberOfElements") int numberOfElements
    ) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public PagedEntity(final List<T> content, final Pageable pageable, final long total) {
        super(content, pageable, total);
    }

    public PagedEntity(final List<T> content) {
        super(content);
    }

    public PagedEntity() {
        super(new ArrayList<>());
    }

    public PagedEntity(final Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    @Override
    public <U> PagedEntity<U> map(final Function<? super T, ? extends U> converter) {
        return new PagedEntity<>(getConvertedContent(converter), getPageable(), getTotalElements());
    }
}
