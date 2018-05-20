package ec.animal.adoption.models.jpa;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import ec.animal.adoption.domain.state.State;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

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


    private JpaState() {
        // required by jpa
    }

    public JpaState(State state) {
        this();
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

        if (id != null ? !id.equals(jpaState.id) : jpaState.id != null) return false;
        if (stateName != null ? !stateName.equals(jpaState.stateName) : jpaState.stateName != null) return false;
        return state != null ? state.equals(jpaState.state) : jpaState.state == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (stateName != null ? stateName.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
