package ec.animal.adoption.domain.characteristics;

import ec.animal.adoption.domain.characteristics.temperament.Temperament;

import java.util.*;

public class Characteristics {

    private Size size;
    private PhysicalActivity physicalActivity;
    private Set<FriendlyWith> friendlyWith;
    private Set<Temperament> temperaments;

    public Characteristics(
            Size size, PhysicalActivity physicalActivity, List<Temperament> temperaments, FriendlyWith ... friendlyWith
    ) {
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.friendlyWith = new HashSet<>(Arrays.asList(friendlyWith));
        this.temperaments = new HashSet<>(temperaments);
    }

    public Size getSize() {
        return size;
    }

    public PhysicalActivity getPhysicalActivity() {
        return physicalActivity;
    }

    public List<FriendlyWith> getFriendlyWith() {
        return new ArrayList<>(this.friendlyWith);
    }

    public List<Temperament> getTemperaments() {
        return new ArrayList<>(temperaments);
    }
}
