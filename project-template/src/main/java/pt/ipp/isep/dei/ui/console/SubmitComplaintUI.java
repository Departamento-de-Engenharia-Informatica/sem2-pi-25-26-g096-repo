package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.SubmitComplaintController;
import pt.ipp.isep.dei.domain.Complaint;
import pt.ipp.isep.dei.domain.PoliticalAgent;
import pt.ipp.isep.dei.domain.PoliticalRole;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class SubmitComplaintUI implements Runnable {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final SubmitComplaintController controller;

    public SubmitComplaintUI() {
        controller = new SubmitComplaintController();
    }

    @Override
    public void run() {
        System.out.println("\n\n--- Submit Complaint -------------------");

        PoliticalAgent selectedAgent = selectPoliticalAgent();
        PoliticalRole selectedRole = selectPoliticalRole();
        LocalDate complaintDate = requestComplaintDate();
        String description = requestComplaintDescription();

        Optional<Complaint> complaint = controller.submitComplaint(selectedAgent.getAgentId(), selectedRole.getName(),
                complaintDate, description);

        if (complaint.isPresent()) {
            System.out.printf("\nComplaint submitted successfully. Id: %s | Status: %s%n",
                    complaint.get().getComplaintId(), complaint.get().getStatus());
        } else {
            System.out.println("\nComplaint not submitted.");
        }
    }

    private PoliticalAgent selectPoliticalAgent() {
        List<PoliticalAgent> agents = controller.getPoliticalAgents();
        int index = readSelection("Select political agent", agents);
        return agents.get(index);
    }

    private PoliticalRole selectPoliticalRole() {
        List<PoliticalRole> roles = controller.getPoliticalRoles();
        int index = readSelection("Select political role", roles);
        return roles.get(index);
    }

    private int readSelection(String prompt, List<?> options) {
        Scanner scanner = new Scanner(System.in);
        int answer = -1;
        while (answer < 1 || answer > options.size()) {
            System.out.println(prompt + ":");
            for (int i = 0; i < options.size(); i++) {
                System.out.printf("  %d - %s%n", i + 1, options.get(i));
            }
            System.out.print("Type your option: ");
            if (scanner.hasNextInt()) {
                answer = scanner.nextInt();
            } else {
                scanner.next();
            }
        }
        return answer - 1;
    }

    private LocalDate requestComplaintDate() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Complaint date (dd-MM-yyyy): ");
            String value = scanner.nextLine();
            try {
                LocalDate date = LocalDate.parse(value, DATE_FORMATTER);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("Date cannot be in the future.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date format. Use dd-MM-yyyy.");
            }
        }
    }

    private String requestComplaintDescription() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Complaint description: ");
            String value = scanner.nextLine();
            if (value != null && !value.isBlank()) {
                return value;
            }
            System.out.println("Description is mandatory.");
        }
    }
}
