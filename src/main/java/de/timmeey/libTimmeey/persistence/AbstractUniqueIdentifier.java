package de.timmeey.libTimmeey.persistence;

import java.util.Arrays;
import lombok.experimental.Accessors;

/**
 * UniqueIdentifier.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
@Accessors(fluent = true)
public abstract class AbstractUniqueIdentifier<T> implements
    UniqueIdentifier<T> {
    private final byte[] bytes;

    protected AbstractUniqueIdentifier() {
        this.bytes = this.generateNewId();
    }

    protected AbstractUniqueIdentifier(final byte[] id) {
        this.bytes = id;
    }

    protected byte[] bytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final AbstractUniqueIdentifier<T> that =
            (AbstractUniqueIdentifier<T>) o;

        return Arrays.equals(this.bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return this.bytes.hashCode();
    }

    abstract protected byte[] generateNewId();
}
