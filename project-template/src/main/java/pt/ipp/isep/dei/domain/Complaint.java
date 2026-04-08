package pt.ipp.isep.dei.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Complaint {

    private final String complaintId;
    private final Citizen citizen;
    private final PoliticalAgent politicalAgent;
    private final PoliticalRole politicalRole;
    private final LocalDate complaintDate;
    private final String description;
    private final ComplaintStatus status;

    public Complaint(String complaintId, Citizen citizen, PoliticalAgent politicalAgent, PoliticalRole politicalRole,
                     LocalDate complaintDate, String description) {
        if (complaintId == null || complaintId.isBlank()) {
            throw new IllegalArgumentException("Complaint id cannot be null or blank.");
        }
        if (citizen == null) {
            throw new IllegalArgumentException("Citizen cannot be null.");
        }
        if (politicalAgent == null) {
            throw new IllegalArgumentException("Political agent cannot be null.");
        }
        if (politicalRole == null) {
            throw new IllegalArgumentException("Political role cannot be null.");
        }
        if (complaintDate == null) {
            throw new IllegalArgumentException("Complaint date cannot be null.");
        }
        if (complaintDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Complaint date cannot be in the future.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Complaint description cannot be null or blank.");
        }

        this.complaintId = complaintId;
        this.citizen = citizen;
        this.politicalAgent = politicalAgent;
        this.politicalRole = politicalRole;
        this.complaintDate = complaintDate;
        this.description = description;
        this.status = ComplaintStatus.SUBMITTED;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complaint)) {
            return false;
        }
        Complaint complaint = (Complaint) o;
        return complaintId.equals(complaint.complaintId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(complaintId);
    }

    public Complaint clone() {
        return new Complaint(this.complaintId, this.citizen.clone(), this.politicalAgent.clone(),
                this.politicalRole.clone(), this.complaintDate, this.description);
    }
}
