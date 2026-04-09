package pt.ipp.isep.dei.domain;

import java.util.Objects;

public class Citizen {

    private final String citizenId;

    public Citizen(String citizenId) {
        if (citizenId == null || citizenId.isBlank()) {
            throw new IllegalArgumentException("Citizen id cannot be null or blank.");
        }
        this.citizenId = citizenId;
    }

    public String getCitizenId() {
        return citizenId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Citizen)) {
            return false;
        }
        Citizen citizen = (Citizen) o;
        return citizenId.equals(citizen.citizenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(citizenId);
    }

    public Citizen clone() {
        return new Citizen(this.citizenId);
    }
}
