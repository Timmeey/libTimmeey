package de.timmeey.libTimmeey.random;

import org.cactoos.Text;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * RandomText.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 1.0.1
 */
public class RandomWord implements Text {

    private final UncheckedScalar<UncheckedText> word;
    private final int minLength;
    private final int maxLength;

    public RandomWord(final int minLength, final int maxLength) {

        word = new UncheckedScalar<>(
            new StickyScalar<>(
                () -> new UncheckedText(
                    new TextOf(randomWord()))
            )
        );

        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    private String randomWord() {
        char[] word = new char[new RandomNumber(this.minLength,
            this.maxLength).value()];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + new RandomNumber(0, 28)
                .value());
        }
        return new String(word);

    }

    @Override
    public String asString() {
        return word.value().asString();
    }

    @Override
    public int compareTo(final Text o) {
        throw new UnsupportedOperationException("#compareTo()");
    }
}
