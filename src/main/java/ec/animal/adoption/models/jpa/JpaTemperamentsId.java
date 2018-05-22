package ec.animal.adoption.models.jpa;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class JpaTemperamentsId implements Serializable {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "characteristics_id", nullable = false)
    private JpaCharacteristics jpaCharacteristics;

    @NotNull
    private String temperament;

    private JpaTemperamentsId() {
        // Required by jpa
    }

    public JpaTemperamentsId(String temperament, JpaCharacteristics jpaCharacteristics) {
        this();
        this.temperament = temperament;
        this.jpaCharacteristics = jpaCharacteristics;
    }

    public @NotNull String getTemperament() {
        return temperament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaTemperamentsId that = (JpaTemperamentsId) o;

        if (jpaCharacteristics != null ? !jpaCharacteristics.equals(that.jpaCharacteristics) : that.jpaCharacteristics != null)
            return false;
        return temperament != null ? temperament.equals(that.temperament) : that.temperament == null;
    }

    @Override
    public int hashCode() {
        int result = jpaCharacteristics != null ? jpaCharacteristics.hashCode() : 0;
        result = 31 * result + (temperament != null ? temperament.hashCode() : 0);
        return result;
    }
}
