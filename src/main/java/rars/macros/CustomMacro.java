package rars.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Represents a macro definition. Macros are shortcuts to insert large code fast.
 *
 * @author Meister Reporter
 */
public class CustomMacro {

    public static final String ARG_REGISTER = "r";
    public static final String ARG_NUMBER = "d";
    public static final String ARG_LABEL = "a";

    public static final String ARGUMENT_REGEX = "%[ard][0-9]+";
    public static final String SPECIFIC_ARGUMENT_REGEX = "%[ard]";

    private final String name;
    private final String code;
    private final int numberOfArguments;
    private final List<String> argumentTypes;

    public CustomMacro(String name, String code, int numberOfArguments) {
        this.name = name;
        this.code = code;
        this.numberOfArguments = numberOfArguments;
        argumentTypes = new ArrayList<>();
        fillArgumentTypes();
    }

    /**
     * Serializes the macro to a string that can be saved to a file.
     *
     * @return The save string.
     */
    public String toSaveString() {
        return "%s|%s|%d".formatted(name, code, numberOfArguments);
    }

    /**
     * Deserializes a macro from a save string.
     *
     * @param saveString The save string.
     *
     * @return The macro.
     */
    public static CustomMacro fromSaveString(String saveString) {
        String[] parts = saveString.split("\\|");
        if (parts.length > 3) {
            throw new IllegalArgumentException("Invalid save string: " + saveString);
        }
        return new CustomMacro(parts[0], parts[1], Integer.parseInt(parts[2]));
    }

    private void fillArgumentTypes() {
        argumentTypes.clear();
        List<MatchResult> matches = Pattern.compile(CustomMacro.ARGUMENT_REGEX).matcher(code).results().toList();
        List<String> arguments = new ArrayList<>();
        for (MatchResult result : matches) {
            if (!arguments.contains(result.group())) {
                arguments.add(result.group());
                argumentTypes.add(result.group().substring(1, 2));
            }
        }
    }

    ////////////
    // Getter //
    ////////////

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getNumberOfArguments() {
        return numberOfArguments;
    }

    public List<String> getArgumentTypes() {
        return argumentTypes;
    }
}
