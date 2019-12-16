package com.leroymerlin.commit;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommitMessageTest {

    @Test
    public void testFormatCommit() {
        CommitMessage commitMessage = new CommitMessage(
                ChangeType.FIX,
                "ngStyle",
                "skip setting empty value when new style has the property",
                "Previously, all the properties in oldStyles are set to empty value once. Using AngularJS with jQuery 3.3.1, this disables the CSS transition as reported in jquery/jquery#4185.",
                "",
                "#16709"
        );
        String expected = "fix(ngStyle): skip setting empty value when new style has the property\n" +
                "\n" +
                "Previously, all the properties in oldStyles are set to empty value once.\n" +
                "Using AngularJS with jQuery 3.3.1, this disables the CSS transition as\n" +
                "reported in jquery/jquery#4185.\n" +
                "\n" +
                "Closes #16709";
        assertEquals(expected, commitMessage.getMessage());
    }

    @Test
    public void testFormatCommitResolve() {
        CommitMessage commitMessage = new CommitMessage("fix(ngStyle): skip setting empty value when new style has the property\n" +
                "\n" +
                "Previously, all the properties in oldStyles are set to empty value once.\n" +
                "Using AngularJS with jQuery 3.3.1, this disables the CSS transition as\n" +
                "reported in jquery/jquery#4185.\n" +
                "\n" +
                "Closes #16709");
        assertEquals(commitMessage.getChangeType(), ChangeType.FIX);
        assertEquals(commitMessage.getChangeScope(), "ngStyle");
        assertEquals(commitMessage.getShortDescription(), "skip setting empty value when new style has the property");
        assertEquals(commitMessage.getLongDescription(), "Previously, all the properties in oldStyles are set to empty value once. Using AngularJS with jQuery 3.3.1, this disables the CSS transition as reported in jquery/jquery#4185.");
        assertEquals(commitMessage.getBreakingChanges(), "");
        assertEquals(commitMessage.getClosedIssues(), "#16709");
    }

    @Test
    public void testFormatCommit_withoutScope() {
        CommitMessage commitMessage = new CommitMessage(
                ChangeType.STYLE,
                "",
                "fix eslint error",
                "",
                "",
                ""
        );
        String expected = "style: fix eslint error\n\n";
        assertEquals(expected, commitMessage.getMessage());
    }

    @Test
    public void testFormatCommit_withoutScopeResolve() {
        CommitMessage commitMessage = new CommitMessage("style: fix eslint error\n\n");
        assertEquals(commitMessage.getChangeType(), ChangeType.STYLE);
        assertEquals(commitMessage.getChangeScope(), "");
        assertEquals(commitMessage.getShortDescription(), "fix eslint error");
        assertEquals(commitMessage.getLongDescription(), "");
        assertEquals(commitMessage.getBreakingChanges(), "");
        assertEquals(commitMessage.getClosedIssues(), "");
    }

    @Test
    public void testFormatCommit_withMultipleClosedIssues() {
        CommitMessage commitMessage = new CommitMessage(
                ChangeType.FEAT,
                "$route",
                "add support for the `reloadOnUrl` configuration option",
                "Enables users to specify that a particular route should not be reloaded after a URL change.",
                "",
                "#7925,#15002"
        );
        String expected = "feat($route): add support for the `reloadOnUrl` configuration option\n" +
                "\n" +
                "Enables users to specify that a particular route should not be reloaded\n" +
                "after a URL change.\n" +
                "\n" +
                "Closes #7925\n" +
                "Closes #15002";
        assertEquals(expected, commitMessage.getMessage());
    }

    @Test
    public void testFormatCommit_withMultipleClosedIssuesResolve() {
        CommitMessage commitMessage = new CommitMessage("feat($route): add support for the `reloadOnUrl` configuration option\n" +
                "\n" +
                "Enables users to specify that a particular route should not be reloaded\n" +
                "after a URL change.\n" +
                "\n" +
                "Closes #7925\n" +
                "Closes #15002");
        assertEquals(commitMessage.getChangeType(), ChangeType.FEAT);
        assertEquals(commitMessage.getChangeScope(), "$route");
        assertEquals(commitMessage.getShortDescription(), "add support for the `reloadOnUrl` configuration option");
        assertEquals(commitMessage.getLongDescription(), "Enables users to specify that a particular route should not be reloaded after a URL change.");
        assertEquals(commitMessage.getBreakingChanges(), "");
        assertEquals(commitMessage.getClosedIssues(), "#7925,#15002");
    }

    @Test
    public void testFormatCommit_withLongBreakingChange() {
        CommitMessage commitMessage = new CommitMessage(
                ChangeType.FEAT,
                "",
                "break everything",
                "",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ""
        );
        String expected = "feat: break everything\n" +
                "\n" +
                "\n" +
                "\n" +
                "BREAKING CHANGE: Lorem ipsum dolor sit amet, consectetur adipiscing\n" +
                "elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi\n" +
                "ut aliquip ex ea commodo consequat. Duis aute irure dolor in\n" +
                "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla\n" +
                "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa\n" +
                "qui officia deserunt mollit anim id est laborum.";
        assertEquals(expected, commitMessage.getMessage());
    }

    @Test
    public void testFormatCommit_withLongBreakingChangeResolve() {
        CommitMessage commitMessage = new CommitMessage("feat: break everything\n" +
                "\n" +
                "\n" +
                "\n" +
                "BREAKING CHANGE: Lorem ipsum dolor sit amet, consectetur adipiscing\n" +
                "elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi\n" +
                "ut aliquip ex ea commodo consequat. Duis aute irure dolor in\n" +
                "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla\n" +
                "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa\n" +
                "qui officia deserunt mollit anim id est laborum.");
        assertEquals(commitMessage.getChangeType(), ChangeType.FEAT);
        assertEquals(commitMessage.getChangeScope(), "");
        assertEquals(commitMessage.getShortDescription(), "break everything");
        assertEquals(commitMessage.getLongDescription(), "");
        assertEquals(commitMessage.getBreakingChanges(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        assertEquals(commitMessage.getClosedIssues(), "");
    }
}