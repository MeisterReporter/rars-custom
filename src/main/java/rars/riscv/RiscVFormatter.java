package rars.riscv;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A formatter for RISC-V assembly code. It will format the code according to specific settings like indentation, spaces
 * etc.
 *
 * @author Meister Reporter
 */
public class RiscVFormatter {

    private static final int TAB_SIZE = 8;

    private static final String SUBROUTINE_LINE_START = "(\\s|\\t)*(\\w+:|\\.\\w+).*";
    private static final String LOOK_FOR_NON_SPACE_COMMA = ",(?!\\s)";
    private static final String DETECT_COMMENT = "#.*";
    private static final String IS_PURE_COMMENT_LINE = "(\\s|\\t)*#.*";

    private static final String LINE_SEPARATOR = "\n";

    // TODO: Make these settings changeable
    private static boolean SPACE_INDENTATION = true;
    private static boolean END_WITH_EMPTY_LINE = true;
    private static int INDENTATION = 4;

    private final String source;

    private int longestLine;

    public RiscVFormatter(String input) {
        this.source = input;
    }

    public String format() {
        StringBuilder formatted = new StringBuilder();
        // Format the lines according to the rules
        for (String line : source.lines().toList()) {
            formatted.append(formatLine(line));
            formatted.append(LINE_SEPARATOR);
        }
        // Format the comments
        String halfFormatted = formatted.toString();
        formatted = new StringBuilder();
        for (String line : halfFormatted.lines().toList()) {
            formatted.append(formatLineComment(line));
            formatted.append(LINE_SEPARATOR);
        }
        if (!END_WITH_EMPTY_LINE) formatted.replace(formatted.length() - 1, formatted.length(), "");
        return formatted.toString();
    }

    private String formatLine(String line) {
        String newLine = String.valueOf(line);
        // Detect the type of line
        if (line.matches(SUBROUTINE_LINE_START)) {
            // This line should not lead with spaces
            newLine = newLine.trim();
        } else if (!line.matches(IS_PURE_COMMENT_LINE)) {
            // Any parameter in this line should be separated by a comma and space
            newLine = newLine.replaceAll(LOOK_FOR_NON_SPACE_COMMA, ", ");
            // This line should load with spaces
            newLine = newLine.trim();
            if (SPACE_INDENTATION) {
                newLine = " ".repeat(INDENTATION) + newLine;
            } else {
                newLine = "\t".repeat(INDENTATION) + newLine;
            }
        }
        // Measure the length of the line
        int lineLength = getLineLength(newLine);
        if (lineLength > longestLine) longestLine = lineLength;
        return newLine;
    }

    private String formatLineComment(String line) {
        String newLine = String.valueOf(line);
        if (!line.matches(IS_PURE_COMMENT_LINE)) {
            Pattern p = Pattern.compile(DETECT_COMMENT);
            Matcher matcher = p.matcher(newLine);
            if (matcher.find()) {
                String comment = matcher.group().replace(LINE_SEPARATOR, "");
                int commentIndex = newLine.indexOf(comment);
                int lineLength = getLineLength(newLine);
                int numberOfSpaces = (longestLine + TAB_SIZE) - lineLength;
                // Clear existing spacing
                newLine = newLine.substring(0, commentIndex).stripTrailing().replace(LINE_SEPARATOR, "");
                // Add new spacing
                String tabs = " ".repeat(numberOfSpaces);
                newLine = newLine + tabs + comment;
            }
        }
        return newLine;
    }

    private int getLineLength(String line) {
        if (!line.matches(IS_PURE_COMMENT_LINE)) {
            Pattern p = Pattern.compile(DETECT_COMMENT);
            Matcher matcher = p.matcher(line);
            if (matcher.find()) {
                String comment = matcher.group();
                int commentIndex = line.indexOf(comment);
                // Clear existing spacing
                return line.substring(0, commentIndex).stripTrailing().length();
            }
        }
        return 0;
    }
}
