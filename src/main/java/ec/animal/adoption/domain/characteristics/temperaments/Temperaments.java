package ec.animal.adoption.domain.characteristics.temperaments;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Temperaments {

    @JsonProperty("sociability")
    private Sociability sociability;

    @JsonProperty("docility")
    private Docility docility;

    @JsonProperty("balance")
    private Balance balance;

    @JsonCreator
    public Temperaments(
            @JsonProperty("sociability") Sociability sociability,
            @JsonProperty("docility") Docility docility,
            @JsonProperty("balance") Balance balance) {
        this.sociability = sociability;
        this.docility = docility;
        this.balance = balance;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return sociability == null && docility == null && balance == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Temperaments that = (Temperaments) o;

        if (sociability != that.sociability) return false;
        if (docility != that.docility) return false;
        return balance == that.balance;
    }

    @Override
    public int hashCode() {
        int result = sociability != null ? sociability.hashCode() : 0;
        result = 31 * result + (docility != null ? docility.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }

    public Sociability getSociability() {
        return sociability;
    }

    public Docility getDocility() {
        return docility;
    }

    public Balance getBalance() {
        return balance;
    }
}
