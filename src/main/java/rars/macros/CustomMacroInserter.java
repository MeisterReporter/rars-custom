package rars.macros;

import rars.Globals;
import rars.venus.editors.jeditsyntax.JEditTextArea;

import java.util.List;

/**
 * This class checks if a line is a macro. And also replaces the line with the macro code if requested.
 *
 * @author Meister Reporter
 */
public class CustomMacroInserter {

    /**
     * Checks if a line is a macro. A macro should be formatted like this: macro_name arg1, arg2, arg3 ...
     *
     * @param line The line to check
     *
     * @return The found macro if the line is a macro, null otherwise.
     */
    public static CustomMacro isLineAMacro(String line) {
        line = line.trim();
        String[] parts = line.split(",");
        List<CustomMacro> macros = Globals.getSettings().loadAllMacros();
        for (CustomMacro macro : macros) {
            if (parts.length > 0 && parts[0].contains(macro.getName())
                    && parts.length == macro.getNumberOfArguments()) {
                return macro;
            }
        }
        return null;
    }

    /**
     * Replaces the macro with the macro code.
     *
     * @param textArea The text area to replace the macro in.
     */
    public static void replaceMacro(JEditTextArea textArea) {
        int caret = textArea.getCaretLine();
        String line = textArea.getLineText(caret);
        CustomMacro macro = isLineAMacro(line);
        if (macro != null) {
            line = line.trim();
            String[] parts = line.split(",");
            if (parts.length > 0) {
                parts[0] = parts[0].replace(macro.getName() + " ", "");
                String sourceCode = macro.getCode();
                for (int i = 0; i < macro.getNumberOfArguments(); i++) {
                    sourceCode = sourceCode.replaceAll(CustomMacro.SPECIFIC_ARGUMENT_REGEX + i, parts[i].trim());
                }
                textArea.select(textArea.getLineStartOffset(caret), Math.max(0, textArea.getLineEndOffset(caret) - 1));
                textArea.setSelectedText(sourceCode);
            }
        }
    }

}
