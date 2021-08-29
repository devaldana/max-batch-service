package net.musicaudience.batch;

import net.musicaudience.domain.Artist;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class ArtistMapper implements FieldSetMapper<Artist> {
    private static final int TRUE = 1;

    @Override
    public Artist mapFieldSet(final FieldSet fieldSet) {
        return Artist.builder()
                     .id(fieldSet.readLong(1))
                     .name(fieldSet.readString(2))
                     .isActual(fieldSet.readInt(3) == TRUE)
                     .url(fieldSet.readString(4))
                     .typeId(fieldSet.readLong(5))
                     .build();
    }
}