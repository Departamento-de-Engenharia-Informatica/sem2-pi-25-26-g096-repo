package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Citizen;
import pt.ipp.isep.dei.domain.Complaint;
import pt.ipp.isep.dei.domain.PoliticalAgent;
import pt.ipp.isep.dei.domain.PoliticalRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplaintRepository {

    private final List<Complaint> complaints;
    private final List<PoliticalAgent> politicalAgents;
    private final List<PoliticalRole> politicalRoles;
    private int sequence;

    public ComplaintRepository() {
        complaints = new ArrayList<>();
        politicalAgents = new ArrayList<>();
        politicalRoles = new ArrayList<>();
        sequence = 0;

        bootstrapPoliticalAgents();
        bootstrapPoliticalRoles();
    }

    public List<PoliticalAgent> getPoliticalAgents() {
        List<PoliticalAgent> clones = new ArrayList<>();
        for (PoliticalAgent politicalAgent : politicalAgents) {
            clones.add(politicalAgent.clone());
        }
        return clones;
    }

    public List<PoliticalRole> getPoliticalRoles() {
        List<PoliticalRole> clones = new ArrayList<>();
        for (PoliticalRole role : politicalRoles) {
            clones.add(role.clone());
        }
        return clones;
    }

    public Optional<Complaint> submit(String citizenId, String politicalAgentId, String politicalRoleName,
                                      LocalDate complaintDate, String description) {

        Optional<PoliticalAgent> politicalAgent = findPoliticalAgentById(politicalAgentId);
        Optional<PoliticalRole> politicalRole = findPoliticalRoleByName(politicalRoleName);

        if (politicalAgent.isEmpty() || politicalRole.isEmpty()) {
            return Optional.empty();
        }

        Citizen citizen = new Citizen(citizenId);
        Complaint complaint = new Complaint(generateComplaintId(), citizen, politicalAgent.get(), politicalRole.get(),
                complaintDate, description);

        complaints.add(complaint.clone());
        return Optional.of(complaint);
    }

    private Optional<PoliticalAgent> findPoliticalAgentById(String politicalAgentId) {
        for (PoliticalAgent agent : politicalAgents) {
            if (agent.getAgentId().equalsIgnoreCase(politicalAgentId)) {
                return Optional.of(agent);
            }
        }
        return Optional.empty();
    }

    private Optional<PoliticalRole> findPoliticalRoleByName(String politicalRoleName) {
        for (PoliticalRole role : politicalRoles) {
            if (role.getName().equalsIgnoreCase(politicalRoleName)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }

    private String generateComplaintId() {
        sequence++;
        return String.format("CMP-%04d", sequence);
    }

    private void bootstrapPoliticalAgents() {
        politicalAgents.add(new PoliticalAgent("PA-001", "Alex Silva"));
        politicalAgents.add(new PoliticalAgent("PA-002", "Maria Costa"));
        politicalAgents.add(new PoliticalAgent("PA-003", "Joao Ferreira"));
    }

    private void bootstrapPoliticalRoles() {
        politicalRoles.add(new PoliticalRole("DEPUTY"));
        politicalRoles.add(new PoliticalRole("MAYOR"));
        politicalRoles.add(new PoliticalRole("MINISTER"));
    }
}
