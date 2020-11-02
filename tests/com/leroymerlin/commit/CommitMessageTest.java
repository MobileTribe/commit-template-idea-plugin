package com.leroymerlin.commit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommitMessageTest {

    @Test
    public void testFormatCommit() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FIX, "ngStyle",
                "skip setting empty value when new style has the property",
                "Previously, all the properties in oldStyles are set to empty value once. Using AngularJS with jQuery 3.3.1, this disables the CSS transition as reported in jquery/jquery#4185.",
                null, "#16709", true, false);
        String expected = "fix(ngStyle): skip setting empty value when new style has the property\n" +
                "\n" +
                "Previously, all the properties in oldStyles are set to empty value once.\n" +
                "Using AngularJS with jQuery 3.3.1, this disables the CSS transition as\n" +
                "reported in jquery/jquery#4185.\n" +
                "\n" +
                "Closes #16709";
        check(commitMessage, expected);
    }

    @Test
    public void testFormatCommit_withoutScope() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.STYLE, null,
                "fix eslint error", null, null, "", true, false);
        String expected = "style: fix eslint error";
        check(commitMessage, expected);
    }

    @Test
    public void testFormatCommit_withMultipleClosedIssues() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, "$route",
                "add support for the `reloadOnUrl` configuration option",
                "Enables users to specify that a particular route should not be reloaded after a URL change.",
                "", "#7925,#15002", true, false);
        String expected = "feat($route): add support for the `reloadOnUrl` configuration option\n" +
                "\n" +
                "Enables users to specify that a particular route should not be reloaded\n" +
                "after a URL change.\n" +
                "\n" +
                "Closes #7925\n" +
                "Closes #15002";
        check(commitMessage, expected);
    }

    @Test
    public void testFormatCommit_withLongBreakingChange() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, null, "break everything", null,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "", true, false);
        String expected = "feat: break everything\n" +
                "\n" +
                "BREAKING CHANGE: Lorem ipsum dolor sit amet, consectetur adipiscing\n" +
                "elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi\n" +
                "ut aliquip ex ea commodo consequat. Duis aute irure dolor in\n" +
                "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla\n" +
                "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa\n" +
                "qui officia deserunt mollit anim id est laborum.";
        check(commitMessage, expected);
    }

    @Test
    public void testParseCommit_invalidFormat() {
        CommitMessage commitMessage = CommitMessage.parse("lorem ipsum");
        assertEquals(null, commitMessage.getChangeType());
    }

    @Test
    public void testFormatCommit_addNumberSignIfMissing() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, "$route",
                "add support for the `reloadOnUrl` configuration option",
                "",
                "", "7925, #15002 , https://github.com/o/r/issues/15003 ", true, false);
        String expected = "feat($route): add support for the `reloadOnUrl` configuration option\n" +
                "\n" +
                "Closes #7925\n" +
                "Closes #15002\n" +
                "Closes https://github.com/o/r/issues/15003";
        assertEquals(expected, commitMessage.toString());
    }

    @Test
    public void testFormatCommit_noWrap() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, null, "break everything", null,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "", false, false);
        String expected = "feat: break everything\n" +
                "\n" +
                "BREAKING CHANGE: Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        check(commitMessage, expected);
    }

    @Test
    public void testFormatCommit_skipCI() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.DOCS, null, "edit README", null,
                "", "", false, true);
        String expected = "docs: edit README\n" +
                "\n" +
                "[skip ci]";
        check(commitMessage, expected);
    }

    private void check(CommitMessage commitMessage, String output) {
        checkFormat(commitMessage, output);
        checkParse(commitMessage, output);
    }

    private void checkFormat(CommitMessage commitMessage, String output) {
        assertEquals(output, commitMessage.toString());
    }

    private void checkParse(CommitMessage commitMessage, String output) {
        CommitMessage actual = CommitMessage.parse(output);
        assertEquals(commitMessage.getChangeType(), actual.getChangeType());
        assertEquals(commitMessage.getChangeScope(), actual.getChangeScope());
        assertEquals(commitMessage.getShortDescription(), actual.getShortDescription());
        // FIXME should we remove newlines?
        // assertEquals(expected.getLongDescription(), actual.getLongDescription());
        // assertEquals(expected.getBreakingChanges(), actual.getBreakingChanges());
        assertEquals(commitMessage.getClosedIssues(), actual.getClosedIssues());
        assertEquals(commitMessage.isSkipCI(), actual.isSkipCI());
    }
}