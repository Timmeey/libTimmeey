package de.timmeey.libTimmeey.color;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id$
 * @since ${VERSION}
 */
public class RGBColorTest {
    @Test
    public void testAsColorBlue() throws Exception {
        assertThat(new RGBColor(0, 0, 255).asRGB()).containsExactly(0, 0, 255);
    }

    @Test
    public void testAsColorGreen() throws Exception {
        assertThat(new RGBColor(0, 255, 0).asRGB()).containsExactly(0, 255, 0);
    }

    @Test
    public void testAsColorRed() throws Exception {
        assertThat(new RGBColor(255, 0, 0).asRGB()).containsExactly(255, 0, 0);
    }
}
