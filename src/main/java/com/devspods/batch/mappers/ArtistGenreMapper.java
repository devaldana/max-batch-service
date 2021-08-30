package com.devspods.batch.mappers;

import com.devspods.domain.ArtistGenre;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import static com.devspods.util.Constants.TRUE;

public class ArtistGenreMapper implements FieldSetMapper<ArtistGenre> {
    @Override
    public ArtistGenre mapFieldSet(final FieldSet fieldSet) {
        return fieldSet.getFieldCount() < 3 ? null : ArtistGenre.builder()
                                                                .genreId(fieldSet.readLong(1))
                                                                .artistId(fieldSet.readLong(2))
                                                                .primary(fieldSet.readInt(3) == TRUE)
                                                                .build();
    }
}
