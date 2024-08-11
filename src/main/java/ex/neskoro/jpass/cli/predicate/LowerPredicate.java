package ex.neskoro.jpass.cli.predicate;

import java.util.function.Predicate;

public class LowerPredicate implements Predicate<Character> {
    @Override
    public boolean test(Character character) {
        return Character.isLowerCase(character);
    }
}
