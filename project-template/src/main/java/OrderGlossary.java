import java.nio.file.*;
import java.util.*;
import java.io.IOException;

public class OrderGlossary {

    public static void main(String[] args) throws IOException {
        String entryFile = "docs\\system-documentation\\global-artifacts\\01.requirements-engineering\\glossary.md";
        String outFile   = "docs\\system-documentation\\global-artifacts\\01.requirements-engineering\\glossary.md";

        List<String> lines = Files.readAllLines(Path.of(entryFile));

        List<String> preface = new ArrayList<>();
        List<String> dataRows = new ArrayList<>();

        boolean seenTable = false;

        for (String line : lines) {
            String t = line.trim();

            if (t.startsWith("|")) {
                seenTable = true;

                if (t.contains("**_TEA_**") || t.contains("Description_**")) {
                    continue;
                }

                if (t.matches("^\\|[:\\- ]+\\|[:\\- ]+\\|[:\\- ]+\\|$")) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                if (parts.length >= 5) {
                    dataRows.add(line);
                }
            } else {
                if (!seenTable) {
                    preface.add(line);
                }
            }
        }

        dataRows.sort(Comparator.comparing(OrderGlossary::extractEnglishTerm, String.CASE_INSENSITIVE_ORDER));

        List<String> out = new ArrayList<>();

        if (preface.isEmpty()) {
            out.add("# Glossary");
            out.add("");
        } else {
            out.addAll(preface);
            if (!preface.get(preface.size() - 1).isBlank()) {
                out.add("");
            }
        }

        out.add("| **_TEA_** (EN) | **_TEA_** (PT) | **_Description_** (EN) |");
        out.add("|:------------------------|:-----------------|:--------------------------------------------|");
        out.addAll(dataRows);

        Files.write(Path.of(outFile), out);
    }

    private static String extractEnglishTerm(String row) {
        String[] parts = row.split("\\|", -1);
        if (parts.length > 1) {
            return cleanMarkdown(parts[1].trim());
        }
        return "";
    }

    private static String cleanMarkdown(String text) {
        return text.replace("*", "").replace("_", "").trim();
    }
}