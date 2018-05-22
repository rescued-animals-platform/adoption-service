package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.FriendlyWith;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "friendly_with")
public class JpaFriendlyWith {

    @EmbeddedId
    private JpaFriendlyWithId jpaFriendlyWithId;

    private JpaFriendlyWith() {
        // Required by jpa
    }

    public JpaFriendlyWith(FriendlyWith friendlyWith, JpaCharacteristics jpaCharacteristics) {
        this();
        this.jpaFriendlyWithId = new JpaFriendlyWithId(friendlyWith.name(), jpaCharacteristics);
    }

    public FriendlyWith toFriendlyWith() {
        return FriendlyWith.valueOf(this.jpaFriendlyWithId.getFriendlyWith());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaFriendlyWith that = (JpaFriendlyWith) o;

        return jpaFriendlyWithId != null ? jpaFriendlyWithId.equals(that.jpaFriendlyWithId) : that.jpaFriendlyWithId == null;
    }

    @Override
    public int hashCode() {
        return jpaFriendlyWithId != null ? jpaFriendlyWithId.hashCode() : 0;
    }
}
