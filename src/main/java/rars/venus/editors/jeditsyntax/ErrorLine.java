package rars.venus.editors.jeditsyntax;

/**
 * Represents data about an error or warning is a specific line of code.
 *
 * @param error      The error message
 * @param line       The line
 * @param errorStart The start of the error
 * @param errorEnd   The end of the error
 * @param isWarning  Whether the error is a warning
 *
 * @author Meister Reporter
 */
public record ErrorLine(String error, int line, int errorStart, int errorEnd, boolean isWarning) {
}
