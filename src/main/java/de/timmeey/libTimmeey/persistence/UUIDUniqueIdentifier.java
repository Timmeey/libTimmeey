package de.timmeey.libTimmeey.persistence;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * UUIDUniqueIdentifier.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
public final class UUIDUniqueIdentifier extends
    AbstractUniqueIdentifier<String> {
    private static final int UUID_BYTE_LENGTH = 16;

    public UUIDUniqueIdentifier() {
        super();
    }

    public UUIDUniqueIdentifier(String id) {
        super(convertUUIDToBytes(UUID.fromString(id)));
    }

    @Override
    public String id() {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(this.bytes());
        final Long most = byteBuffer.getLong();
        final Long least = byteBuffer.getLong();

        return new UUID(most, least).toString();
    }

    @Override
    protected byte[] generateNewId() {
        return convertUUIDToBytes(UUID.randomUUID());

    }

    private static byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[UUID_BYTE_LENGTH]);
        bb.putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
