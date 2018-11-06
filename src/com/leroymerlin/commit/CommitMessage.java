package com.leroymerlin.commit;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author Damien Arrachequesne <damien.arrachequesne@gmail.com>
 */
class CommitMessage {
    private final String content;

    CommitMessage(ChangeType changeType, String changeScope, String shortDescription, String longDescription, String closedIssues, String breakingChanges) {
        this.content = buildContent(changeType, changeScope, shortDescription, longDescription, closedIssues, breakingChanges);
    }

    private String buildContent(ChangeType changeType, String changeScope, String shortDescription, String longDescription, String closedIssues, String breakingChanges) {
        StringBuilder builder = new StringBuilder();
        builder.append(changeType.label());
        if (isNotBlank(changeScope)) {
            builder
                    .append('(')
                    .append(changeScope)
                    .append(')');
        }
        builder
                .append(": ")
                .append(shortDescription)
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(breakLines(longDescription, 100));

        if (isNotBlank(breakingChanges)) {
            builder
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append("BREAKING CHANGE: ")
                    .append(breakingChanges);
        }

        if (isNotBlank(closedIssues)) {
            builder.append(System.lineSeparator());
            for (String closedIssue : closedIssues.split(",")) {
                builder
                        .append(System.lineSeparator())
                        .append("Closes ")
                        .append(closedIssue);
            }
        }

        return builder.toString();
    }

    private static String breakLines(String input, int maxLineLength) {
        String[] tokens = input.split("\\s+");
        StringBuilder output = new StringBuilder(input.length());
        int lineLength = 0;
        for (int i = 0; i < tokens.length; i++) {
            String word = tokens[i];

            boolean shouldAddNewLine = lineLength + (" " + word).length() > maxLineLength;
            if (shouldAddNewLine) {
                if (i > 0) {
                    output.append(System.lineSeparator());
                }
                lineLength = 0;
            }
            boolean shouldAddSpace = i < tokens.length - 1 &&
                    (lineLength + (word + " ").length() + tokens[i + 1].length() <= maxLineLength);
            if (shouldAddSpace) {
                word += " ";
            }
            output.append(word);
            lineLength += word.length();
        }
        return output.toString();
    }

    @Override
    public String toString() {
        return content;
    }
}