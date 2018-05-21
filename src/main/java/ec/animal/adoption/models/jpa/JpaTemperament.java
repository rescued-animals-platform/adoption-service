package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.temperament.Temperament;

public class JpaTemperament {

    private final String temperament;

    public JpaTemperament(Temperament temperament) {
        this.temperament = temperament.name();
    }

    public Temperament toTemperament() {
        return Temperament.valueOf(this.temperament);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaTemperament that = (JpaTemperament) o;

        return temperament != null ? temperament.equals(that.temperament) : that.temperament == null;
    }

    @Override
    public int hashCode() {
        return temperament != null ? temperament.hashCode() : 0;
    }
}
