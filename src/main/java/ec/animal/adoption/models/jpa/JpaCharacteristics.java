package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class JpaCharacteristics {

    private final Timestamp timestamp;
    private final String size;
    private final String physicalActivity;
    private final List<String> temperaments;
    private final List<String> friendlyWith;

    public JpaCharacteristics(Characteristics characteristics) {
        timestamp = Timestamp.valueOf(LocalDateTime.now());
        size = characteristics.getSize().name();
        physicalActivity = characteristics.getPhysicalActivity().name();
        temperaments = characteristics.getTemperaments().stream().map(Temperament::name).collect(Collectors.toList());
        friendlyWith = characteristics.getFriendlyWith().stream().map(FriendlyWith::name).collect(Collectors.toList());
    }

    public Characteristics toCharacteristics() {
        List<Temperament> temperaments = this.temperaments.stream().map(Temperament::valueOf).collect(Collectors.toList());
        List<FriendlyWith> friendlyWith = this.friendlyWith.stream().map(FriendlyWith::valueOf).collect(Collectors.toList());
        return new Characteristics(
                Size.valueOf(this.size),
                PhysicalActivity.valueOf(this.physicalActivity),
                temperaments,
                friendlyWith.toArray(new FriendlyWith[friendlyWith.size()])
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaCharacteristics that = (JpaCharacteristics) o;

        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (physicalActivity != null ? !physicalActivity.equals(that.physicalActivity) : that.physicalActivity != null)
            return false;
        if (temperaments != null ? !temperaments.equals(that.temperaments) : that.temperaments != null) return false;
        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (physicalActivity != null ? physicalActivity.hashCode() : 0);
        result = 31 * result + (temperaments != null ? temperaments.hashCode() : 0);
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}
