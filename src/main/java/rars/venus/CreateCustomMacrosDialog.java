package rars.venus;

import rars.Globals;
import rars.macros.CustomMacro;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * A dialog to create and edit custom macros.
 *
 * @author Meister Reporter
 */
public class CreateCustomMacrosDialog extends JDialog {

    private JSplitPane splitPane;

    private JList<String> macroList;

    private JPanel rightPanel;
    private JPanel macroNameContainer;
    private JTextField macroName;
    private EditPane macroCode;

    public CreateCustomMacrosDialog(Frame owner, String title, boolean modality) {
        super(owner, title, modality);
        splitPane = new JSplitPane();
        add(splitPane);
        macroList = new JList<>();
        macroList.addListSelectionListener(e -> {
            String selectedName = macroList.getSelectedValue();
            List<CustomMacro> macros = Globals.getSettings().loadAllMacros();
            for (CustomMacro macro : macros) {
                if (macro.getName().equals(selectedName)) {
                    macroName.setText(macro.getName());
                    macroCode.setSourceCode(macro.getCode(), true);
                    return;
                }
            }
            macroName.setText("");
            macroCode.setSourceCode("# Enter Macro code here", true);
        });
        JScrollPane scrollPane = new JScrollPane(macroList);
        scrollPane.setMinimumSize(new Dimension(200, 100));
        splitPane.setLeftComponent(scrollPane);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setMinimumSize(new Dimension(400, 100));
        // Macro Name
        macroNameContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        macroNameContainer.add(new JLabel("Macro Name: "));
        macroName = new JTextField();
        macroName.setPreferredSize(new Dimension(150, 25));
        macroNameContainer.add(macroName);
        rightPanel.add(macroNameContainer);
        // Description
        JPanel descriptionContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextArea description = new JTextArea(
                """
                In the following editor you can define your custom macro code. You can also use placeholders for arguments.
                Format placeholder like this: %r0 for register, %d1 for numbers and %a2 for text.
                It is important to NOT mix types of arguments: %r0 and %a0 is invalid.
                """);
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setPreferredSize(new Dimension(800, 78));
        JLabel lb = new JLabel();
        Font f = lb.getFont();
        description.setFont(f);
        description.setBorder(lb.getBorder());
        description.setBackground(new Color(lb.getBackground().getRGB(), true));
        description.setForeground(new Color(lb.getForeground().getRGB(), true));
        description.setOpaque(lb.isOpaque());
        descriptionContainer.add(description);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(descriptionContainer);
        // Macro Code
        macroCode = new EditPane(Globals.getGui());
        macroCode.setMinimumSize(new Dimension(400, 350));
        macroCode.setCheckSyntaxErrors(false);
        macroCode.setSourceCode("# Enter Macro code here", true);
        macroCode.setShowLineNumbersEnabled(true);
        macroCode.setName("Macro Code");
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(macroCode);

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String macroName = this.macroName.getText();
                String macroCode = this.macroCode.getSource();
                if (macroName.isBlank() || macroCode.isBlank()) {
                    String emptyFields = (macroName.isBlank() ? "name" : "") + (macroCode.isBlank() ? " and code" : "");
                    JOptionPane.showConfirmDialog(this,
                            "Cannot save macro with empty " + emptyFields,
                            "Empty fields", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<MatchResult> matches = Pattern.compile(CustomMacro.ARGUMENT_REGEX).matcher(macroCode).results()
                        .toList();
                List<String> arguments = new ArrayList<>();
                for (MatchResult result : matches) {
                    String group = result.group();
                    // Check for syntax errors
                    int index = Integer.parseInt(group.substring(2));
                    if (index < arguments.size()) {
                        // The argument was already declared
                        // Now check if there is a type mismatch
                        String type = group.substring(1, 2);
                        String declaredType = arguments.get(index).substring(1, 2);
                        String typeName = switch (declaredType) {
                            case "r" -> "Register";
                            case "d" -> "Number";
                            case "a" -> "Text";
                            default -> "unknown";
                        };
                        if (!type.equals(declaredType)) {
                            JOptionPane.showConfirmDialog(this,
                                    "Argument \"%s\" was already declared with a different type \"%s\"".formatted(group, typeName),
                                    "Type mismatch", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    // Everything is fine
                    if (!arguments.contains(result.group())) arguments.add(group);
                }
                CustomMacro macro = new CustomMacro(macroName, macroCode, arguments.size());
                Globals.getSettings().saveMacro(macro);
                fillMacroList();
            } catch (Exception ex) {
                JOptionPane.showConfirmDialog(this,
                        "An error occurred while saving the macro: " + ex.getMessage(),
                        "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton cancelButton = new JButton("Close");
        cancelButton.addActionListener(e -> dispose());
        buttonContainer.add(cancelButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(8, 0)));
        buttonContainer.add(saveButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(buttonContainer);

        splitPane.setRightComponent(rightPanel);

        fillMacroList();

        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fillMacroList() {
        int lastIndex = Math.max(0, macroList.getSelectedIndex());
        List<CustomMacro> macros = Globals.getSettings().loadAllMacros();
        List<String> macroNames = new ArrayList<>();
        macroNames.add("New Macro");
        for (CustomMacro macro : macros) {
            macroNames.add(macro.getName());
        }
        macroList.setListData(macroNames.toArray(String[]::new));
        macroList.setSelectedIndex(lastIndex);
    }
}
