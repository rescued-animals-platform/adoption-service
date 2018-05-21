package ec.animal.adoption.domain.characteristics;

import ec.animal.adoption.domain.characteristics.temperament.Temperament;

import java.util.*;

public class Characteristics {

    private final Size size;
    private final PhysicalActivity physicalActivity;
    private final Set<Temperament> temperaments;
    private final Set<FriendlyWith> friendlyWith;

    public Characteristics(
            Size size, PhysicalActivity physicalActivity, List<Temperament> temperaments, FriendlyWith... friendlyWith
    ) {
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.friendlyWith = new HashSet<>(Arrays.asList(friendlyWith));
        this.temperaments = new HashSet<>(temperaments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Characteristics that = (Characteristics) o;

        if (size != that.size) return false;
        if (physicalActivity != that.physicalActivity) return false;
        if (temperaments != null ? !temperaments.equals(that.temperaments) : that.temperaments != null) return false;
        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    public int hashCode() {
        int result = size != null ? size.hashCode() : 0;
        result = 31 * result + (physicalActivity != null ? physicalActivity.hashCode() : 0);
        result = 31 * result + (temperaments != null ? temperaments.hashCode() : 0);
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}
