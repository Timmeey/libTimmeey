package de.timmeey.libTimmeey.color;

import java.awt.Color;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

/**
 * HSLColor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class HSLColor implements TColor {
    final UncheckedScalar<Color> color;

    public HSLColor(final TColor color) {
        this.color = new UncheckedScalar<>(
            new StickyScalar<>(() -> color.asColor())
        );
    }

    public HSLColor(final Color color) {
        this.color = new UncheckedScalar<Color>(new StickyScalar<>(() ->
            color));
    }

    public HSLColor(final float[] hsl) {
        this(hsl[0], hsl[1], hsl[2]);
    }

    public HSLColor(final float hue, final float saturation, final float
        lightness) {
        this.color = new UncheckedScalar<>(
            new StickyScalar<>(() ->
                new RGBColor
                    (hslToRgb(hue, saturation, lightness)).asColor()));
    }

    /**
     * Attributions to https://gist.github.com/mjackson/5311256
     * Converts an HSL color value to RGB. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes h, s, and l are contained in the set [0, 1] and
     * returns r, g, and b in the set [0, 255].
     * @param h The hue
     * @param s The saturation
     * @param l The lightness
     * @return Array           The RGB representation
     */
    private int[] hslToRgb(float h, float s, float l) {
        float r, g, b;

        if (s == 0f) {
            r = g = b = l; // achromatic
        } else {
            float q = l < 0.5f ? l * (1f + s) : l + s - l * s;
            float p = 2f * l - q;

            r = hue2rgb(p, q, h + 1f / 3f);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1f / 3f);
        }

        return new int[]{Math.round(r * 255), Math.round(g * 255), Math.round
            (b * 255)};
    }

    private float hue2rgb(float p, float q, float t) {
        if (t < 0f) t += 1f;
        if (t > 1f) t -= 1f;
        if (t < 1f / 6f) return p + (q - p) * 6f * t;
        if (t < 1f / 2f) return q;
        if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6f;
        return p;
    }

    @Override
    public Color asColor() {
        return this.color.value();
    }

}
