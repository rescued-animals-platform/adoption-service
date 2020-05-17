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

package ec.animal.adoption.repository.jpa;

import ec.animal.adoption.repository.jpa.model.JpaAnimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings({"PMD.UseObjectForClearerAPI"})
public interface JpaAnimalRepository extends PagingAndSortingRepository<JpaAnimal, UUID> {

    Optional<JpaAnimal> findByIdAndJpaOrganizationId(UUID animalId, UUID organizationId);

    Optional<JpaAnimal> findByClinicalRecordAndJpaOrganizationId(String clinicalRecord, UUID organizationId);

    boolean existsByClinicalRecordAndJpaOrganizationId(String clinicalRecord, UUID organizationId);

    Page<JpaAnimal> findAllByJpaOrganizationId(UUID organizationId, Pageable pageable);

    Page<JpaAnimal> findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
            String stateName, String species, String physicalActivity, String size, Pageable pageable
    );
}
