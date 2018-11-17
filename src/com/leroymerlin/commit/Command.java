package com.leroymerlin.commit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class Command {
    private final File workingDirectory;
    private final String command;

    Command(File workingDirectory, String command) {
        this.workingDirectory = workingDirectory;
        this.command = command;
    }

    static class Result {
        static Result ERROR = new Result(-1);

        private final int exitValue;
        private final List<String> output;

        Result(int exitValue) {
            this.exitValue = exitValue;
            this.output = null;
        }

        Result(int exitValue, List<String> output) {
            this.exitValue = exitValue;
            this.output = output;
        }

        boolean isSuccess() {
            return exitValue == 0;
        }

        List<String> getOutput() {
            return output;
        }
    }

    Result execute() {
        try {
            Process process = new ProcessBuilder("/bin/sh", "-c", this.command)
                    .directory(workingDirectory)
                    .start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            List<String> output = reader.lines().collect(Collectors.toList());

            process.waitFor(2, TimeUnit.SECONDS);
            process.destroy();
            process.waitFor();

            return new Result(process.exitValue(), output);
        } catch (Exception e) {
            return Result.ERROR;
        }
    }

}