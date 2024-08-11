package ex.neskoro.jpass.cli.config;

import static picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {
    @Override
    public String[] getVersion() {
        return new String[]{"jpass 1.1", "ex.neskoro©", "2024"};
    }
}