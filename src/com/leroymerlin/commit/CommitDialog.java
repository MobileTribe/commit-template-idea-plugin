package com.leroymerlin.commit;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Damien Arrachequesne
 */
public class CommitDialog extends DialogWrapper {

    private final CommitPanel panel;

    CommitDialog(@Nullable Project project, CommitMessage commitMessage) {
        super(project);
        panel = new CommitPanel(project, commitMessage);
        setTitle("Commit");
        setOKButtonText("OK");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel.getMainPanel();
    }

    CommitMessage getCommitMessage() {
        return panel.getCommitMessage();
    }

}
