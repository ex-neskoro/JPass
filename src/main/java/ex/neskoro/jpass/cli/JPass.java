package ex.neskoro.jpass.cli;


import ex.neskoro.jpass.cli.config.PrintExceptionMessageHandler;
import ex.neskoro.jpass.cli.config.VersionProvider;
import ex.neskoro.jpass.cli.predicate.*;
import picocli.CommandLine;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static picocli.CommandLine.*;

@Command(name = "jpass",
        scope = ScopeType.INHERIT,
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class,
        description = "JPass project - cli tool for generating temporary passwords",
        sortOptions = false)
public class JPass implements Callable<Integer> {

    // https://www.asciitable.com/
    // ! char
    private static final int LOWER_BOUND = 33;
    // z char
    private static final int UPPER_BOUND = 122;
    private static final Random RANDOM = ThreadLocalRandom.current();
    private static final Random SECURE_RANDOM;
    private static final String SECURE_ALGO = "NativePRNGNonBlocking";

    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstance(SECURE_ALGO);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Option(names = {"-l", "--length"},
            required = true,
            description = "Password length",
            defaultValue = "20",
            showDefaultValue = Help.Visibility.ALWAYS)
    private int length;

    @Option(names = {"--lower-case"},
            description = "Flag to include lowercase characters in password")
    private boolean isLowerPresent;

    @Option(names = {"--upper-case"},
            description = "Flag to include uppercase characters in password")
    private boolean isUpperPresent;

    @Option(names = {"-d", "--digits"},
            description = "Flag to include digit characters in password")
    private boolean isDigitsPresent;

    @Option(names = {"-s", "--special"},
            description = "Flag to include special characters in password")
    private boolean isSpecialPresent;

    @Option(names = {"-e"},
            description = "Flag to exclude some of characters in password"
    )
    private boolean isExcludedPresent;

    @Option(names = {"--excludedChars"},
            description = "String of characters to exclude from generated password",
            defaultValue = "/|<>;,@$`~?=-+\\",
            showDefaultValue = Help.Visibility.ALWAYS)
    private String excludedCharacters;

    @Option(names = {"-a", "--all"},
            description = "Flag to activate all symbols in password - it overrides all other flags",
            showDefaultValue = Help.Visibility.ALWAYS)
    private boolean allPredicates;

    @Option(names = {"--secure"},
            description = "Flag to activate secure random generator for characters. May cause reduce performance",
            showDefaultValue = Help.Visibility.ALWAYS)
    private boolean isSecure;

    private ArrayList<Predicate<Character>> predicates;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new JPass())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .setSubcommandsCaseInsensitive(true)
                .setOptionsCaseInsensitive(true)
                .setExecutionExceptionHandler(new PrintExceptionMessageHandler())
                .execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        setupPredicates();
        if (predicates.isEmpty()) {
            throw new RuntimeException("You should use at least one predicate flag. See --help for more information.");
        }

        Stream<Character> stream = prepareRandom(isSecure).ints(LOWER_BOUND, UPPER_BOUND)
                .boxed()
                .map(i -> (char) i.intValue());

        if (isExcludedPresent) {
            stream = stream.filter(new ExcludedPredicate(excludedCharacters));
        }

        String pass = stream.filter(getCombinedPredicate(predicates))
                .limit(length)
                .map(Object::toString)
                .collect(Collectors.joining());

        System.out.print(pass);

        return 0;
    }

    private void setupPredicates() {
        predicates = new ArrayList<>();

        if (allPredicates) {
            predicates.add(new LowerPredicate());
            predicates.add(new UpperPredicate());
            predicates.add(new DigitPredicate());
            predicates.add(new SpecialPredicate());
            return;
        }

        if (isLowerPresent) {
            predicates.add(new LowerPredicate());
        }
        if (isUpperPresent) {
            predicates.add(new UpperPredicate());
        }
        if (isDigitsPresent) {
            predicates.add(new DigitPredicate());
        }
        if (isSpecialPresent) {
            predicates.add(new SpecialPredicate());
        }
    }

    private Random prepareRandom(boolean isSecure) {
        return isSecure ? SECURE_RANDOM : RANDOM;
    }

    private Predicate<Character> getCombinedPredicate(ArrayList<Predicate<Character>> predicates) {
        Predicate<Character> combinedPredicate = character -> false;
        for (Predicate<Character> predicate : predicates) {
            combinedPredicate = combinedPredicate.or(predicate);
        }
        return combinedPredicate;
    }

}