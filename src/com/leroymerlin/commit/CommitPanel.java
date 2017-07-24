package com.leroymerlin.commit;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

/**
 * @author Damien Arrachequesne
 */
public class CommitPanel {
    private JPanel mainPanel;
    private JComboBox changeType;
    private JTextField changeScope;
    private JTextField shortDescription;
    private JTextArea longDescription;
    private JTextField closedIssues;
    private JTextField breakingChanges;

    CommitPanel(DialogWrapper dialog) {
        for (ChangeType type : ChangeType.values()) {
            changeType.addItem(type);
        }
    }

    JPanel getMainPanel() {
        return mainPanel;
    }

    String getChangeType() {
        ChangeType type = (ChangeType) changeType.getSelectedItem();
        return type.label();
    }

    String getChangeScope() {
        return changeScope.getText().trim();
    }

    String getShortDescription() {
        return shortDescription.getText().trim();
    }

    String getLongDescription() {
        return longDescription.getText().trim();
    }

    String getBreakingChanges() {
        return breakingChanges.getText().trim();
    }

    String getClosedIssues() {
        return closedIssues.getText().trim();
    }
}
