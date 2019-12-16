package com.leroymerlin.commit;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;

import javax.swing.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Damien Arrachequesne
 */
public class CommitPanel {

    private JPanel mainPanel;
    private JComboBox changeType;
    private JComboBox changeScope;
    private JTextField shortDescription;
    private JTextArea longDescription;
    private JTextArea breakingChanges;
    private JTextField closedIssues;

    CommitPanel(Project project, CommitMessage commitMessage) {
        for (ChangeType type : ChangeType.values()) {
            changeType.addItem(type);
        }
        File workingDirectory = VfsUtil.virtualToIoFile(project.getBaseDir());
        GitLogQuery.Logs logs = new GitLogQuery(workingDirectory).execute();
        if (logs.isSuccess()) {
            logs.getScopes().forEach(changeScope::addItem);
        }

        if (commitMessage != null) {
            resolveCommitMessage(commitMessage);
        }
    }

    JPanel getMainPanel() {
        return mainPanel;
    }

    CommitMessage getCommitMessage() {
        return new CommitMessage(
                (ChangeType) changeType.getSelectedItem(),
                (String) changeScope.getSelectedItem(),
                shortDescription.getText().trim(),
                longDescription.getText().trim(),
                breakingChanges.getText().trim(),
                closedIssues.getText().trim()
        );
    }

    private void resolveCommitMessage(CommitMessage commitMessage) {
        changeType.setSelectedItem(commitMessage.getChangeType());
        changeScope.setSelectedItem(commitMessage.getChangeScope());
        shortDescription.setText(commitMessage.getShortDescription());
        longDescription.setText(commitMessage.getLongDescription());
        breakingChanges.setText(commitMessage.getBreakingChanges());
        closedIssues.setText(commitMessage.getClosedIssues());
    }
}
