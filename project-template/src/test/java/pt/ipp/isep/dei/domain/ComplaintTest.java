package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComplaintTest {

    @Test
    void ensureComplaintDateCannotBeInFuture() {
        Citizen citizen = new Citizen("citizen@this.app");
        PoliticalAgent politicalAgent = new PoliticalAgent("PA-001", "Alex Silva");
        PoliticalRole role = new PoliticalRole("MAYOR");

        assertThrows(IllegalArgumentException.class,
                () -> new Complaint("CMP-0001", citizen, politicalAgent, role,
                        LocalDate.now().plusDays(1), "Description"));
    }

    @Test
    void ensureComplaintStatusStartsAsSubmitted() {
        Citizen citizen = new Citizen("citizen@this.app");
        PoliticalAgent politicalAgent = new PoliticalAgent("PA-001", "Alex Silva");
        PoliticalRole role = new PoliticalRole("MAYOR");

        Complaint complaint = new Complaint("CMP-0001", citizen, politicalAgent, role,
                LocalDate.now(), "Description");

        assertEquals(ComplaintStatus.SUBMITTED, complaint.getStatus());
    }
}
