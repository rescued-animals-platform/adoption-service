package ec.animal.adoption.adapter.jpa.model;

import ec.animal.adoption.domain.model.organization.Organization;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JpaOrganizationMapper {

    JpaOrganizationMapper MAPPER = Mappers.getMapper(JpaOrganizationMapper.class);

    @Mapping(source = "id", target = "organizationId")
    Organization toOrganization(JpaOrganization jpaOrganization);

    @InheritInverseConfiguration
    JpaOrganization toJpaOrganization(Organization organization);
}
