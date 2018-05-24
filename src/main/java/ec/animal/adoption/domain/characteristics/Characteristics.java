package ec.animal.adoption.domain.characteristics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;
import ec.animal.adoption.domain.validators.Temperaments;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

public class Characteristics {

    @NotNull(message = "Size is required")
    @JsonProperty("size")
    private final Size size;

    @NotNull(message = "Physical activity is required")
    @JsonProperty("physicalActivity")
    private final PhysicalActivity physicalActivity;

    @NotEmpty(message = "At least one temperament is required")
    @Temperaments
    @JsonProperty("temperaments")
    private Set<Temperament> temperaments;

    @JsonProperty("friendlyWith")
    private final Set<FriendlyWith> friendlyWith;

    @JsonIgnore
    private UUID animalUuid;

    @JsonCreator
    public Characteristics(
            @JsonProperty("size") Size size,
            @JsonProperty("physicalActivity") PhysicalActivity physicalActivity,
            @JsonProperty("temperaments") List<Temperament> temperaments,
            @JsonProperty("friendlyWith") FriendlyWith... friendlyWith
    ) {
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.friendlyWith = new HashSet<>(Arrays.asList(friendlyWith));
        if (temperaments != null) {
            this.temperaments = new HashSet<>(temperaments);
        }
    }

    public Size getSize() {
        return size;
    }

    public PhysicalActivity getPhysicalActivity() {
        return physicalActivity;
    }

    public List<Temperament> getTemperaments() {
        return new ArrayList<>(temperaments);
    }

    public List<FriendlyWith> getFriendlyWith() {
        return new ArrayList<>(friendlyWith);
    }

    public UUID getAnimalUuid() {
        return animalUuid;
    }

    public void setAnimalUuid(UUID animalUuid) {
        this.animalUuid = animalUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Characteristics that = (Characteristics) o;

        if (size != that.size) return false;
        if (physicalActivity != that.physicalActivity) return false;
        if (temperaments != null ? !temperaments.equals(that.temperaments) : that.temperaments != null) return false;
        if (friendlyWith != null ? !friendlyWith.equals(that.friendlyWith) : that.friendlyWith != null) return false;
        return animalUuid != null ? animalUuid.equals(that.animalUuid) : that.animalUuid == null;
    }

    @Override
    public int hashCode() {
        int result = size != null ? size.hashCode() : 0;
        result = 31 * result + (physicalActivity != null ? physicalActivity.hashCode() : 0);
        result = 31 * result + (temperaments != null ? temperaments.hashCode() : 0);
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        result = 31 * result + (animalUuid != null ? animalUuid.hashCode() : 0);
        return result;
    }
}
