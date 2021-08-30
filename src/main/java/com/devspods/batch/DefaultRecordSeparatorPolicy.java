package com.devspods.batch;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

import static com.devspods.util.Constants.RECORD_SEPARATOR_CHARACTER;

public class DefaultRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {
    @Override
    public String postProcess(final String line) {
        // Removing the last ASCII character line separator before sending line to the mapper.
        var lineSeparatorIndex = line.lastIndexOf(RECORD_SEPARATOR_CHARACTER);
        return lineSeparatorIndex >= 0 ? line.substring(0, lineSeparatorIndex) : line;
    }
}
