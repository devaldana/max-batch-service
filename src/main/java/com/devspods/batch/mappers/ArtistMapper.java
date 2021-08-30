package com.devspods.batch.mappers;

import com.devspods.domain.Artist;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import static com.devspods.util.Constants.TRUE;

public class ArtistMapper implements FieldSetMapper<Artist> {
    @Override
    public Artist mapFieldSet(final FieldSet fieldSet) {
        return fieldSet.getFieldCount() < 5 ? null : Artist.builder()
                                                           .id(fieldSet.readLong(1))
                                                           .name(fieldSet.readString(2))
                                                           .actual(fieldSet.readInt(3) == TRUE)
                                                           .url(fieldSet.readString(4))
                                                           .typeId(fieldSet.readLong(5))
                                                           .build();
    }
}