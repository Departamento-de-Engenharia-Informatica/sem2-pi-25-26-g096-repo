package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Complaint;
import pt.ipp.isep.dei.domain.PoliticalAgent;
import pt.ipp.isep.dei.domain.PoliticalRole;
import pt.ipp.isep.dei.repository.AuthenticationRepository;
import pt.ipp.isep.dei.repository.ComplaintRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.isep.lei.esoft.auth.domain.model.Email;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SubmitComplaintController {

    private ComplaintRepository complaintRepository;
    private AuthenticationRepository authenticationRepository;

    public SubmitComplaintController() {
        getComplaintRepository();
        getAuthenticationRepository();
    }

    public SubmitComplaintController(ComplaintRepository complaintRepository,
                                     AuthenticationRepository authenticationRepository) {
        this.complaintRepository = complaintRepository;
        this.authenticationRepository = authenticationRepository;
    }

    public List<PoliticalAgent> getPoliticalAgents() {
        return getComplaintRepository().getPoliticalAgents();
    }

    public List<PoliticalRole> getPoliticalRoles() {
        return getComplaintRepository().getPoliticalRoles();
    }

    public Optional<Complaint> submitComplaint(String politicalAgentId,
                                               String politicalRole,
                                               LocalDate complaintDate,
                                               String description) {
        String citizenId = getCitizenIdFromSession();
        return getComplaintRepository().submit(citizenId, politicalAgentId, politicalRole, complaintDate, description);
    }

    private ComplaintRepository getComplaintRepository() {
        if (complaintRepository == null) {
            Repositories repositories = Repositories.getInstance();
            complaintRepository = repositories.getComplaintRepository();
        }
        return complaintRepository;
    }

    private AuthenticationRepository getAuthenticationRepository() {
        if (authenticationRepository == null) {
            Repositories repositories = Repositories.getInstance();
            authenticationRepository = repositories.getAuthenticationRepository();
        }
        return authenticationRepository;
    }

    private String getCitizenIdFromSession() {
        Email email = getAuthenticationRepository().getCurrentUserSession().getUserId();
        return email.getEmail();
    }
}
