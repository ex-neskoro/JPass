package ex.neskoro.jpass.cli.predicate;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SpecialPredicate implements Predicate<Character> {
    @Override
    public boolean test(Character character) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        return pattern.matcher(character.toString()).matches();
    }
}
