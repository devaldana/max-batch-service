package net.musicaudience.batch.mappers;

import net.musicaudience.domain.Genre;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class GenreMapper implements FieldSetMapper<Genre> {
    @Override
    public Genre mapFieldSet(final FieldSet fieldSet) {
        return fieldSet.getFieldCount() < 3 ? null : Genre.builder()
                                                          .id(fieldSet.readLong(1))
                                                          .parentId(readParentId(fieldSet))
                                                          .name(fieldSet.readString(3))
                                                          .build();
    }

    private Long readParentId(final FieldSet fieldSet) {
        return isBlank(fieldSet.readString(2)) ? null : fieldSet.readLong(2);
    }
}
