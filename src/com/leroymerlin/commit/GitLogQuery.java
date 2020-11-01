package com.leroymerlin.commit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

class GitLogQuery {
    private static final String GIT_LOG_COMMAND = "git log --all --format=%s";
    private static final Pattern COMMIT_FIRST_LINE_FORMAT = Pattern.compile("^[a-z]+\\((.+)\\):.*");

    private final File workingDirectory;

    GitLogQuery(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    static class Result {
        static Result ERROR = new Result(-1);

        private final int exitValue;
        private final List<String> logs;

        Result(int exitValue) {
            this(exitValue, emptyList());
        }

        Result(int exitValue, List<String> logs) {
            this.exitValue = exitValue;
            this.logs = logs;
        }

        boolean isSuccess() {
            return exitValue == 0;
        }

        public Set<String> getScopes() {
            Set<String> scopes = new HashSet<>();

            this.logs.forEach(s -> {
                Matcher matcher = COMMIT_FIRST_LINE_FORMAT.matcher(s);
                if (matcher.find()) {
                    scopes.add(matcher.group(1));
                }
            });

            return scopes;
        }
    }

    Result execute() {
        try {
            ProcessBuilder processBuilder;
            String osName = System.getProperty("os.name");
            if (osName.contains("Windows")) {
                processBuilder = new ProcessBuilder("cmd", "/C", GIT_LOG_COMMAND);
            } else {
                processBuilder = new ProcessBuilder("sh", "-c", GIT_LOG_COMMAND);
            }

            Process process = processBuilder
                    .directory(workingDirectory)
                    .start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            List<String> output = reader.lines().collect(toList());

            process.waitFor(2, TimeUnit.SECONDS);
            process.destroy();
            process.waitFor();

            return new Result(process.exitValue(), output);
        } catch (Exception e) {
            return Result.ERROR;
        }
    }

}