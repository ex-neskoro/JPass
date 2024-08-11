package ex.neskoro.jpass.cli.predicate;

import java.util.function.Predicate;

public class UpperPredicate implements Predicate<Character> {
    @Override
    public boolean test(Character character) {
        return Character.isUpperCase(character);
    }
}
