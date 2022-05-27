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

package ec.animal.adoption.domain.animal;

import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.animal.characteristics.Size;
import ec.animal.adoption.domain.animal.dto.AnimalDto;
import ec.animal.adoption.domain.organization.Organization;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AnimalRepository {

    Animal create(final AnimalDto animalDto);

    Animal save(final Animal animal);

    Animal getBy(final UUID animalId);

    Animal getBy(final UUID animalId, final Organization organization);

    Optional<Animal> getBy(final String clinicalRecord, final Organization organization);

    boolean exists(final String clinicalRecord, final UUID organizationId);

    PagedEntity<Animal> getAllFor(final Organization organization, final Pageable pageable);

    PagedEntity<Animal> getAllBy(
            final String stateName,
            final Species species,
            final PhysicalActivity physicalActivity,
            final Size size,
            final Pageable pageable
    );
}
