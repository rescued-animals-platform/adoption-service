package ec.animal.adoption.models.jpa;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class JpaFriendlyWithId implements Serializable {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "characteristics_id", nullable = false)
    private JpaCharacteristics jpaCharacteristics;

    @NotNull
    private String friendlyWith;

    private JpaFriendlyWithId() {
        // Required by jpa
    }

    public JpaFriendlyWithId(String friendlyWith, JpaCharacteristics jpaCharacteristics) {
        this();
        this.friendlyWith = friendlyWith;
        this.jpaCharacteristics = jpaCharacteristics;
    }

    public @NotNull String getFriendlyWith() {
        return friendlyWith;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaFriendlyWithId that = (JpaFriendlyWithId) o;

        if (jpaCharacteristics != null ? !jpaCharacteristics.equals(that.jpaCharacteristics) : that.jpaCharacteristics != null)
            return false;
        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    public int hashCode() {
        int result = jpaCharacteristics != null ? jpaCharacteristics.hashCode() : 0;
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}
