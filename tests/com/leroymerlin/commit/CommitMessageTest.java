package com.leroymerlin.commit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommitMessageTest {

    @Test
    public void testFormatCommit() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FIX, "ngStyle",
                "skip setting empty value when new style has the property",
                "Previously, all the properties in oldStyles are set to empty value once. Using AngularJS with jQuery 3.3.1, this disables the CSS transition as reported in jquery/jquery#4185.",
                "#16709", "");
        String expected = "fix(ngStyle): skip setting empty value when new style has the property\n" +
                "\n" +
                "Previously, all the properties in oldStyles are set to empty value once. Using AngularJS with \n" +
                "jQuery 3.3.1, this disables the CSS transition as reported in jquery/jquery#4185.\n" +
                "\n" +
                "Closes #16709";
        assertEquals(commitMessage.toString(), expected);
    }

    @Test
    public void testFormatCommit_withoutScope() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.STYLE, "",
                "fix eslint error", "", "", "");
        String expected = "style: fix eslint error\n\n";
        assertEquals(commitMessage.toString(), expected);
    }

    @Test
    public void testFormatCommit_withMultipleClosedIssues() {
        CommitMessage commitMessage = new CommitMessage(ChangeType.FEAT, "$route",
                "add support for the `reloadOnUrl` configuration option",
                "Enables users to specify that a particular route should not be reloaded after a URL change.",
                "#7925,#15002", "");
        String expected = "feat($route): add support for the `reloadOnUrl` configuration option\n" +
                "\n" +
                "Enables users to specify that a particular route should not be reloaded after a URL change.\n" +
                "\n" +
                "Closes #7925\n" +
                "Closes #15002";
        assertEquals(commitMessage.toString(), expected);
    }
}