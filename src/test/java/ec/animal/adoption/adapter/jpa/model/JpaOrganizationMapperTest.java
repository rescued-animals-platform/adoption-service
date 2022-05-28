package ec.animal.adoption.adapter.jpa.model;

import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.organization.OrganizationFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaOrganizationMapperTest {

    @Test
    void shouldMapOrganizationAndJpaOrganizationCorrectly() {
        Organization expectedOrganization = OrganizationFactory.random().build();

        JpaOrganization jpaOrganization = JpaOrganizationMapper.MAPPER.toJpaOrganization(expectedOrganization);
        Organization actualOrganization = JpaOrganizationMapper.MAPPER.toOrganization(jpaOrganization);

        assertAll(
                () -> assertEquals(expectedOrganization.getOrganizationId(), actualOrganization.getOrganizationId()),
                () -> assertEquals(expectedOrganization.getName(), actualOrganization.getName()),
                () -> assertEquals(expectedOrganization.getCity(), actualOrganization.getCity()),
                () -> assertEquals(expectedOrganization.getEmail(), actualOrganization.getEmail()),
                () -> assertEquals(expectedOrganization.getReceptionAddress(), actualOrganization.getReceptionAddress()),
                () -> assertEquals(expectedOrganization.getAdoptionFormPdfUrl(), actualOrganization.getAdoptionFormPdfUrl())
        );
    }
}