package ex.neskoro.jpass.cli.config;

import picocli.CommandLine;

public class PrintExceptionMessageHandler implements CommandLine.IExecutionExceptionHandler {
    public int handleExecutionException(Exception ex,
                                        CommandLine cmd,
                                        CommandLine.ParseResult parseResult) {

        // bold red error message
        cmd.getErr().println(cmd.getColorScheme().errorText(ex.getMessage()));

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : cmd.getCommandSpec().exitCodeOnExecutionException();
    }
}
