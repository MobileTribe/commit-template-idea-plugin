package com.leroymerlin.commit;

import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author Damien Arrachequesne <damien.arrachequesne@gmail.com>
 */
class CommitMessage {
    private static final int MAX_LINE_LENGTH = 72; // https://stackoverflow.com/a/2120040/5138796

    private ChangeType changeType;
    private String changeScope, shortDescription, longDescription, breakingChanges, closedIssues;
    private String message;

    public CommitMessage(ChangeType changeType, String changeScope, String shortDescription, String longDescription, String breakingChanges, String closedIssues) {
        this.changeType = changeType;
        this.changeScope = changeScope;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.breakingChanges = breakingChanges;
        this.closedIssues = closedIssues;
        this.message = buildMessage();
    }

    public CommitMessage(String message) {
        Pattern pattern = Pattern.compile("^([a-z]+)(\\((.+)\\))?: (.+)");
        Matcher matcher = pattern.matcher(message);
        if (!matcher.find()) return;

        this.changeType = ChangeType.valueOf(matcher.group(1).toUpperCase());
        this.changeScope = matcher.group(3);
        this.shortDescription = matcher.group(4);

        String[] strings = message.split("\n");
        if (strings.length < 2) return;

        int pos = 1;
        StringBuilder stringBuilder;

        stringBuilder = new StringBuilder();
        for (; pos < strings.length; pos++) {
            String lineString = strings[pos];
            if (lineString.startsWith("BREAKING") || lineString.startsWith("Closes")) break;
            stringBuilder.append(lineString).append('\n');
        }
        this.longDescription = stringBuilder.toString().trim();

        stringBuilder = new StringBuilder();
        for (; pos < strings.length; pos++) {
            String lineString = strings[pos];
            if (lineString.startsWith("Closes")) break;
            stringBuilder.append(lineString).append('\n');
        }
        this.breakingChanges = stringBuilder.toString().trim().replace("BREAKING CHANGE: ", "");

        pattern = Pattern.compile("Closes (.+)");
        matcher = pattern.matcher(message);
        stringBuilder = new StringBuilder();
        while (matcher.find()) {
            stringBuilder.append(matcher.group(1)).append(',');
        }
        if (stringBuilder.length() > 0) stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        this.closedIssues = stringBuilder.toString();

        this.message = message;
    }

    private String buildMessage() {
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

    public ChangeType getChangeType() {
        return changeType;
    }

    public String getChangeScope() {
        return notNull(changeScope, "");
    }

    public String getShortDescription() {
        return notNull(shortDescription, "");
    }

    public String getLongDescription() {
        return notNull(longDescription, "");
    }

    public String getBreakingChanges() {
        return notNull(breakingChanges, "");
    }

    public String getClosedIssues() {
        return notNull(closedIssues, "");
    }

    public String getMessage() {
        return notNull(message, "");
    }

    @NotNull
    public static <T> T notNull(@Nullable T value, @NotNull T defaultValue) {
        return value != null ? value : defaultValue;
    }
}