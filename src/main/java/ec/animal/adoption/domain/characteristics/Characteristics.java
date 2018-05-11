package ec.animal.adoption.domain.characteristics;

import java.util.*;

public class Characteristics {

    private Size size;
    private PhysicalActivity physicalActivity;
    private Set<FriendlyWith> friendlyWith;

    public Characteristics(Size size, PhysicalActivity physicalActivity, FriendlyWith ... friendlyWith) {
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.friendlyWith = new HashSet<>(Arrays.asList(friendlyWith));
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
}
