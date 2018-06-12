package ec.animal.adoption.models.jpa;

import org.hibernate.annotations.Type;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class JpaImage {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    protected UUID id;
}
