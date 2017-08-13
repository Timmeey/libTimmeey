package de.timmeey.libTimmeey.math;

import java.util.Iterator;
import org.cactoos.Func;

/**
 * LinearSteppingTransform.
 * Produces an Iterator from start -> end value with intermediate linear
 * changing intermediate values
 * e.g 0->100, steps:10
 * gives 10,20,30,....100
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class LinearSteppingTransform implements Iterator<Double> {

    private final Iterator<Double> result;
    private final Func<Long, Double> stepTransformation;

    public LinearSteppingTransform(final double start, final double end,
        final long
            steps) {

        this.stepTransformation = currentStep ->
            start + (((end - start) / steps) * currentStep);

        this.result = new SteppingTransform<>(steps, stepTransformation);
    }

    @Override
    public boolean hasNext() {
        return this.result.hasNext();
    }

    @Override
    public Double next() {
        return this.result.next();
    }
}
