package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "temperaments")
public class JpaTemperaments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sociability;

    private String docility;

    private String balance;


    private JpaTemperaments() {
        // Required by jpa
    }

    public JpaTemperaments(Temperaments temperaments) {
        this();
        this.sociability = temperaments.getSociability() != null ? temperaments.getSociability().name() : null;
        this.docility = temperaments.getDocility() != null ? temperaments.getDocility().name() : null;
        this.balance = temperaments.getBalance() != null ? temperaments.getBalance().name() : null;
    }

    public Temperaments toTemperaments() {
        return new Temperaments(
                this.sociability != null ? Sociability.valueOf(sociability) : null,
                this.docility != null ? Docility.valueOf(this.docility) : null,
                this.balance != null ? Balance.valueOf(this.balance) : null
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaTemperaments that = (JpaTemperaments) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (sociability != null ? !sociability.equals(that.sociability) : that.sociability != null) return false;
        if (docility != null ? !docility.equals(that.docility) : that.docility != null) return false;
        return balance != null ? balance.equals(that.balance) : that.balance == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sociability != null ? sociability.hashCode() : 0);
        result = 31 * result + (docility != null ? docility.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}
