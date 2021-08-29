package net.musicaudience.batch;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

import static net.musicaudience.Constants.LINE_SEPARATOR_CHARACTER;

public class DefaultRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {
    @Override
    public String postProcess(final String line) {
        return line.substring(0, line.lastIndexOf(LINE_SEPARATOR_CHARACTER));
    }
}
