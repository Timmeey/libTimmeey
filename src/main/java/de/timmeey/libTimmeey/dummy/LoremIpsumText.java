package de.timmeey.libTimmeey.dummy;

import org.cactoos.Text;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;
import org.cactoos.text.UncheckedText;

/**
 * LoremIpsumText.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 1.0.1
 */
public class LoremIpsumText implements Text {

    private final UncheckedScalar<UncheckedText> words;

    public LoremIpsumText(final int words) {
        this.words = new UncheckedScalar<>(
            new StickyScalar<>(() ->
                new UncheckedText(LoremIpsum.getLoremIpsumWords(words))
            )
        );
    }

    @Override
    public String asString() {
        return words.value().asString();
    }

    @Override
    public int compareTo(final Text o) {
        return words.value().compareTo(o);
    }
}
