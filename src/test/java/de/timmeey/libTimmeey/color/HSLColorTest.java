package de.timmeey.libTimmeey.color;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id$
 * @since ${VERSION}
 */
public class HSLColorTest {
    /**
     * ${CLASS} can work.
     * @throws Exception If fails
     */
    @Test
    public void testAsColor() throws Exception {
        HSLColor underTest = new HSLColor((float) 0.98, (float) 1, (float) 0.5);
        assertThat(underTest.asRGB()).isEqualTo(new RGBColor(underTest.asRGB
            ()).asRGB());
    }

    @Test
    public void testAsColorBlue() throws Exception {
        assertThat(new HSLColor(new RGBColor(0, 0, 255).asHSL()).asRGB())
            .containsExactly(0, 0, 255);
    }

    @Test
    public void testAsColorGreen() throws Exception {
        assertThat(new HSLColor(new RGBColor(0, 255, 0).asHSL()).asRGB())
            .containsExactly(0, 255, 0);
    }

    @Test
    public void testAsColorRed() throws Exception {
        assertThat(new HSLColor(new RGBColor(255, 0, 0).asHSL()).asRGB())
            .containsExactly(255, 0, 0);
    }

}
