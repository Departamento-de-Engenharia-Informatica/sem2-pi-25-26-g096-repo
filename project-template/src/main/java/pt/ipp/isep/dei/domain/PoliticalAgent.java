package pt.ipp.isep.dei.domain;

import java.util.Objects;

public class PoliticalAgent {

    private final String agentId;
    private final String name;

    public PoliticalAgent(String agentId, String name) {
        if (agentId == null || agentId.isBlank()) {
            throw new IllegalArgumentException("Agent id cannot be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Agent name cannot be null or blank.");
        }
        this.agentId = agentId;
        this.name = name;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", agentId, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoliticalAgent)) {
            return false;
        }
        PoliticalAgent that = (PoliticalAgent) o;
        return agentId.equals(that.agentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId);
    }

    public PoliticalAgent clone() {
        return new PoliticalAgent(this.agentId, this.name);
    }
}
