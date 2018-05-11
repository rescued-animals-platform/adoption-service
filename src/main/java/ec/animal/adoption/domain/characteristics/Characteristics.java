package ec.animal.adoption.domain.characteristics;

import java.util.Arrays;
import java.util.List;

public class Characteristics {

    private Size size;
    private PhysicalActivity physicalActivity;
    private List<FriendlyWith> friendlyWith;

    public Characteristics(Size size, PhysicalActivity physicalActivity, FriendlyWith ... friendlyWith) {
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.friendlyWith = Arrays.asList(friendlyWith);
    }

    public Size getSize() {
        return size;
    }

    public PhysicalActivity getPhysicalActivity() {
        return physicalActivity;
    }

    public List<FriendlyWith> getFriendlyWith() {
        return friendlyWith;
    }
}
