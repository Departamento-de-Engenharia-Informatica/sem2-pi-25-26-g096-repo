package pt.ipp.isep.dei.domain;

import java.util.Objects;

public class PoliticalRole {

    private final String name;

    public PoliticalRole(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Political role cannot be null or blank.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoliticalRole)) {
            return false;
        }
        PoliticalRole that = (PoliticalRole) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    public PoliticalRole clone() {
        return new PoliticalRole(this.name);
    }
}
