package pt.ipp.isep.dei.repository;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Complaint;
import pt.ipp.isep.dei.domain.ComplaintStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComplaintRepositoryTest {

    @Test
    void ensureSubmitComplaintWorks() {
        ComplaintRepository repository = new ComplaintRepository();

        Optional<Complaint> complaint = repository.submit("citizen@this.app", "PA-001", "MAYOR",
                LocalDate.now(), "Potential conflict of interest");

        assertTrue(complaint.isPresent());
        assertEquals(ComplaintStatus.SUBMITTED, complaint.get().getStatus());
    }

    @Test
    void ensureSubmitComplaintFailsWithInvalidAgent() {
        ComplaintRepository repository = new ComplaintRepository();

        Optional<Complaint> complaint = repository.submit("citizen@this.app", "PA-999", "MAYOR",
                LocalDate.now(), "Potential conflict of interest");

        assertTrue(complaint.isEmpty());
    }
}
