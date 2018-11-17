package com.leroymerlin.commit;

import org.apache.commons.lang.WordUtils;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author Damien Arrachequesne <damien.arrachequesne@gmail.com>
 */
class CommitMessage {
    private static final int MAX_LINE_LENGTH = 72; // https://stackoverflow.com/a/2120040/5138796
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
                .append(WordUtils.wrap(longDescription, MAX_LINE_LENGTH));

        if (isNotBlank(breakingChanges)) {
            builder
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append(WordUtils.wrap("BREAKING CHANGE: " + breakingChanges, MAX_LINE_LENGTH));
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

    @Override
    public String toString() {
        return content;
    }
}