package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.FriendlyWith;

public class JpaFriendlyWith {

    private final String friendlyWith;

    public JpaFriendlyWith(FriendlyWith friendlyWith) {
        this.friendlyWith = friendlyWith.name();
    }

    public FriendlyWith toFriendlyWith() {
        return FriendlyWith.valueOf(this.friendlyWith);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaFriendlyWith that = (JpaFriendlyWith) o;

        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    public int hashCode() {
        return friendlyWith != null ? friendlyWith.hashCode() : 0;
    }
}
