package com.leroymerlin.commit;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;

import javax.swing.*;
import java.io.File;

/**
 * @author Damien Arrachequesne
 */
public class CommitPanel {
    private JPanel mainPanel;
    private JComboBox changeType;
    private JComboBox changeScope;
    private JTextField shortDescription;
    private JTextArea longDescription;
    private JTextField closedIssues;
    private JTextArea breakingChanges;

    CommitPanel(Project project) {
        for (ChangeType type : ChangeType.values()) {
            changeType.addItem(type);
        }
        File workingDirectory = VfsUtil.virtualToIoFile(project.getBaseDir());
        Command.Result result = new Command(workingDirectory, "git log --all --format=%s | grep -Eo '^[a-z]+(\\(.*\\)):.*$' | sed 's/^.*(\\(.*\\)):.*$/\\1/' | sort -n | uniq").execute();
        if (result.isSuccess()) {
            result.getOutput().forEach(changeScope::addItem);
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
                closedIssues.getText().trim(),
                breakingChanges.getText().trim()
        );
    }

}
