package net.musicaudience.batch;

import net.musicaudience.domain.Artist;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import static net.musicaudience.Constants.TRUE;

public class ArtistMapper implements FieldSetMapper<Artist> {
    @Override
    public Artist mapFieldSet(final FieldSet fieldSet) {
        return Artist.builder()
                     .id(fieldSet.readLong(1))
                     .name(fieldSet.readString(2))
                     .actual(fieldSet.readInt(3) == TRUE)
                     .url(fieldSet.readString(4))
                     .typeId(fieldSet.readLong(5))
                     .build();
    }
}