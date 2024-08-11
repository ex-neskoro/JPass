package ex.neskoro.jpass.cli.predicate;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExcludedPredicate implements Predicate<Character> {
    private final Set<Character> excludedCharactersList;

    public ExcludedPredicate(String excludedCharacters) {
        excludedCharactersList = excludedCharacters.chars()
                .boxed()
                .map(i -> (char) i.intValue())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean test(Character character) {
        return !excludedCharactersList.contains(character);
    }
}
