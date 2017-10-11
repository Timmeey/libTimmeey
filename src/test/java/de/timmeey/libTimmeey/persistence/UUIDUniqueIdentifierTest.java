package de.timmeey.libTimmeey.persistence;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id$
 * @since ${VERSION}
 */
public class UUIDUniqueIdentifierTest {
    /**
     * ${CLASS} can work.
     * @throws Exception If fails
     */
    @Test
    public void BackAndForthWorks() throws Exception {
        UUIDUniqueIdentifier uuid = new UUIDUniqueIdentifier();
        Assertions.assertThat(new UUIDUniqueIdentifier(uuid.id()))
            .isEqualToComparingFieldByField(uuid);
    }
}
