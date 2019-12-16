package com.leroymerlin.commit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class GitLogQuery {
    private final File workingDirectory;
    private final String command;

    GitLogQuery(File workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.command = "git log --all --format=%s";
    }

    static class Logs {
        static Logs ERROR = new Logs(-1);

        private final int exitValue;
        private final List<String> logs;

        Logs(int exitValue) {
            this.exitValue = exitValue;
            this.logs = null;
        }

        Logs(int exitValue, List<String> logs) {
            this.exitValue = exitValue;
            this.logs = logs;
        }

        boolean isSuccess() {
            return exitValue == 0;
        }

        List<String> getLogs() {
            return logs;
        }

        public List<String> getScopes() {
            List<String> scopes = new ArrayList<>();

            Pattern pattern = Pattern.compile("^[a-z]+\\((.+)\\):.*");
            getLogs().forEach(s -> {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    String scope = matcher.group(1);
                    if (!scopes.contains(scope)) scopes.add(scope);
                }
            });

            return scopes;
        }
    }

    Logs execute() {
        try {
            ProcessBuilder processBuilder;
            String osName = System.getProperty("os.name");
            if (osName.contains("Windows")) {
                processBuilder = new ProcessBuilder("cmd", "/C", this.command);
            } else {
                processBuilder = new ProcessBuilder("sh", "-c", this.command);
            }

            Process process = processBuilder
                    .directory(workingDirectory)
                    .start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            List<String> output = reader.lines().collect(Collectors.toList());

            process.waitFor(2, TimeUnit.SECONDS);
            process.destroy();
            process.waitFor();

            return new Logs(process.exitValue(), output);
        } catch (Exception e) {
            return Logs.ERROR;
        }
    }

}