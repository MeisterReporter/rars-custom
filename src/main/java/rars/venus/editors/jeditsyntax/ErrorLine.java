package rars.venus.editors.jeditsyntax;

public record ErrorLine(String error, int line, int errorStart, int errorEnd, boolean isWarning) {
}
