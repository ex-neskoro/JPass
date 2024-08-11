package ex.neskoro.jpass.cli.predicate;

import java.util.function.Predicate;

public class DigitPredicate implements Predicate<Character> {
    @Override
    public boolean test(Character character) {
        return Character.isDigit(character);
    }
}
