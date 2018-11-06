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

    CommitMessage getCommitMessage() {
        return new CommitMessage(
                (ChangeType) changeType.getSelectedItem(),
                changeScope.getText().trim(),
                shortDescription.getText().trim(),
                longDescription.getText().trim(),
                breakingChanges.getText().trim(),
                closedIssues.getText().trim()
        );
    }

}
