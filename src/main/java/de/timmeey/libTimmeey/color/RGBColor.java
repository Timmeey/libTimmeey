package de.timmeey.libTimmeey.color;

import java.awt.Color;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

/**
 * RGBColor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class RGBColor implements TColor {
    private final UncheckedScalar<Color> color;

    public RGBColor(final TColor color) {
        this.color = new UncheckedScalar<>(
            new StickyScalar<>(() -> color.asColor())
        );
    }

    public RGBColor(final Color color) {
        this.color = new UncheckedScalar<>(new StickyScalar<>(() -> color));
    }

    public RGBColor(final int[] rgb) {
        this(rgb[0], rgb[1], rgb[2]);
    }

    public RGBColor(final int red, final int green, final int blue) {
        this.color = new UncheckedScalar<>(new StickyScalar<>(() -> new Color
            (red, green, blue)));
    }

    @Override
    public Color asColor() {
        return color.value();
    }
}
