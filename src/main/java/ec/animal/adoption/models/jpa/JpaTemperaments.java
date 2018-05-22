package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.temperament.Temperament;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "temperaments")
public class JpaTemperaments {

    @EmbeddedId
    private JpaTemperamentsId jpaTemperamentsId;

    private JpaTemperaments() {
        // Required by jpa
    }

    public JpaTemperaments(Temperament temperament, JpaCharacteristics jpaCharacteristics) {
        this();
        this.jpaTemperamentsId = new JpaTemperamentsId(temperament.name(), jpaCharacteristics);
    }

    public Temperament toTemperament() {
        return Temperament.valueOf(this.jpaTemperamentsId.getTemperament());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaTemperaments that = (JpaTemperaments) o;

        return jpaTemperamentsId != null ? jpaTemperamentsId.equals(that.jpaTemperamentsId) : that.jpaTemperamentsId == null;
    }

    @Override
    public int hashCode() {
        return jpaTemperamentsId != null ? jpaTemperamentsId.hashCode() : 0;
    }
}
