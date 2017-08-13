package de.timmeey.libTimmeey.math;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id$
 * @since 0.1
 */
public class LinearSteppingTransformTest {
    /**
     * ${CLASS} can work.
     * @throws Exception If fails
     */
    @Test
    public void testNextStep() throws Exception {
        final LinearSteppingTransform underTest = new
            LinearSteppingTransform(0, 100, 1);
        assertThat(underTest.next()).isEqualTo(100);
    }

    @Test
    public void testZeroStep() throws Exception {
        final LinearSteppingTransform underTest = new
            LinearSteppingTransform(0, 100, 0);
        assertThat(underTest.hasNext()).isFalse();
    }

    @Test
    public void testContainsExactlyRight() throws Exception {
        final LinearSteppingTransform underTest = new
            LinearSteppingTransform(0, 100, 2);
        assertThat(underTest).containsExactly(50.0, 100.0);
    }

}
