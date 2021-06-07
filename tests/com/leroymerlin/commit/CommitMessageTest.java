package com.leroymerlin.commit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommitMessageTest {

    @Test
    public void testFormatCommit() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FIX, true, "ngStyle",
                "skip setting empty value when new style has the property",
                "Previously, all the properties in oldStyles are set to empty value once. Using AngularJS with jQuery 3.3.1, this disables the CSS transition as reported in jquery/jquery#4185.",
                null, "#16709", true, false);
        String expected = "fix(ngStyle): skip setting empty value when new style has the property" + System.lineSeparator() +
                System.lineSeparator() +
                "Previously, all the properties in oldStyles are set to empty value once." + System.lineSeparator() +
                "Using AngularJS with jQuery 3.3.1, this disables the CSS transition as" + System.lineSeparator() +
                "reported in jquery/jquery#4185." + System.lineSeparator() +
                System.lineSeparator() +
                "Closes #16709";
        check(commitMessage, expected);
    }

    @Test
    public void testFormatCommit_withoutScope() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.STYLE, true, null,
                "fix eslint error", null, null, "", true, false);
        String expected = "style: fix eslint error";
        check(commitMessage, expected);
    }

    @Test
    public void testFormatCommit_withMultipleClosedIssues() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, true, "$route",
                "add support for the `reloadOnUrl` configuration option",
                "Enables users to specify that a particular route should not be reloaded after a URL change.",
                "", "#7925,#15002", true, false);
        String expected = "feat($route): add support for the `reloadOnUrl` configuration option" + System.lineSeparator() +
                System.lineSeparator() +
                "Enables users to specify that a particular route should not be reloaded" + System.lineSeparator() +
                "after a URL change." + System.lineSeparator() +
                System.lineSeparator() +
                "Closes #7925" + System.lineSeparator() +
                "Closes #15002";
        check(commitMessage, expected);
    }

    @Test
    public void testFormatCommit_withLongBreakingChange() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, true, null, "break everything", null,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "", true, false);
        String expected = "feat: break everything" + System.lineSeparator() +
                System.lineSeparator() +
                "BREAKING CHANGE: Lorem ipsum dolor sit amet, consectetur adipiscing" + System.lineSeparator() +
                "elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." + System.lineSeparator() +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi" + System.lineSeparator() +
                "ut aliquip ex ea commodo consequat. Duis aute irure dolor in" + System.lineSeparator() +
                "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla" + System.lineSeparator() +
                "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa" + System.lineSeparator() +
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
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, true, "$route",
                "add support for the `reloadOnUrl` configuration option",
                "",
                "", "7925, #15002 , https://github.com/o/r/issues/15003 ", true, false);
        String expected = "feat($route): add support for the `reloadOnUrl` configuration option" + System.lineSeparator() +
                System.lineSeparator() +
                "Closes #7925" + System.lineSeparator() +
                "Closes #15002" + System.lineSeparator() +
                "Closes https://github.com/o/r/issues/15003";
        assertEquals(expected, commitMessage.toString());
    }

    @Test
    public void testFormatCommit_noWrap() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, true, null, "break everything", null,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "", false, false);
        String expected = "feat: break everything" + System.lineSeparator() +
                System.lineSeparator() +
                "BREAKING CHANGE: Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        check(commitMessage, expected);
    }

    @Test
    public void testFormatCommit_skipCI() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.DOCS, true, null, "edit README", null,
                "", "", false, true);
        String expected = "docs: edit README" + System.lineSeparator() +
                System.lineSeparator() +
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