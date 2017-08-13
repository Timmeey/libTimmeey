package de.timmeey.libTimmeey.color;

import java.awt.Color;

/**
 * TColor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface TColor {

    /**
     * This color as HSL value according to http://en.wikipedia
     * .org/wiki/HSL_color_space
     * Attributions to https://gist.github.com/mjackson/5311256
     * @return [h, s, l]
     */
    default float[] asHSL() {
        float r = this.asColor().getRed();
        float g = this.asColor().getGreen();
        float b = this.asColor().getBlue();
        r /= 255;
        g /= 255;
        b /= 255;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h, s, l;
        l = (max + min) / 2;

        if (max == min) {
            h = s = 0; // achromatic
        } else {
            float d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

            if (max == r) {
                h = (g - b) / d + (g < b ? 6 : 0);
            } else if (max == g) {
                h = (b - r) / d + 2;
            } else {
                h = (r - g) / d + 4;
            }

            h /= 6;
        }
        return new float[]{h, s, l};
    }

    Color asColor();

    default float[] asHSB() {
        throw new UnsupportedOperationException("#asHSB()");
    }

    default float[] asHSV() {
        throw new UnsupportedOperationException("#asHSB()");
    }

    default int[] asRGB() {
        Color col = this.asColor();
        return new int[]{col.getRed(), col.getGreen(), col.getBlue()};
    }
}
