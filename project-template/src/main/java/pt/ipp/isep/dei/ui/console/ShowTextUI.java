package pt.ipp.isep.dei.ui.console;

import org.apache.commons.lang3.StringUtils;

public class ShowTextUI implements Runnable {
    private String text;

    public ShowTextUI(String text) {
        if (StringUtils.isBlank(text))
            throw new IllegalArgumentException("ShowTextUI does not support null or empty text");

        this.text = text;
    }

    public void run() {
        System.out.println("\n");
        System.out.println(this.text);
        System.out.println("\n");
    }
}