package ec.animal.adoption.domain.characteristics;

public class Characteristics {
    private Size size;
    private PhysicalActivity physicalActivity;

    public Characteristics(Size size, PhysicalActivity physicalActivity) {
        this.size = size;
        this.physicalActivity = physicalActivity;
    }

    public Size getSize() {
        return size;
    }

    public PhysicalActivity getPhysicalActivity() {
        return physicalActivity;
    }
}
