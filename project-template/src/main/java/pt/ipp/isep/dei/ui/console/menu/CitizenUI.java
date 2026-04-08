package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.ui.console.ShowTextUI;
import pt.ipp.isep.dei.ui.console.SubmitComplaintUI;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CitizenUI implements Runnable {

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Submit Complaint", new SubmitComplaintUI()));
        options.add(new MenuItem("Option 2", new ShowTextUI("You have chosen Option 2.")));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n--- CITIZEN MENU -----------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
