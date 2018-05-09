package ec.animal.adoption.models.jpa;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import ec.animal.adoption.domain.state.State;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Objects;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "state")
public class JpaState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stateName;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private State state;

    @SuppressWarnings(value = "unused")
    public JpaState() {
        // required by jpa
    }

    public JpaState(State state) {
        this.stateName = state.getClass().getSimpleName();
        this.state = state;
    }

    public State toState() {
        return this.state;
    }

    public void setState(State state) {
        this.stateName = state.getClass().getSimpleName();
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaState jpaState = (JpaState) o;
        return Objects.equals(id, jpaState.id) &&
                Objects.equals(stateName, jpaState.stateName) &&
                Objects.equals(state, jpaState.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stateName, state);
    }
}
