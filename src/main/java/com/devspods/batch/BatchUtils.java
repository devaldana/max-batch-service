package com.devspods.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;

import static com.devspods.util.Constants.*;

public final class BatchUtils {
    private BatchUtils() {}

    public static <T> Step buildStep(final String stepName,
                                     final String filePath,
                                     final FieldSetMapper<T> fieldSetMapper,
                                     final String query,
                                     final ItemProcessor<T, T> processor,
                                     final StepBuilderFactory stepBuilderFactory,
                                     final DataSource dataSource) {
        var fileReader = getFileReader(fieldSetMapper, filePath);
        var jdbcBatchWriter = getJdbcBatchWriter(dataSource, query);
        var taskExecutor = new SimpleAsyncTaskExecutor();
        return stepBuilderFactory.get(stepName).<T, T>chunk(DEFAULT_CHUNK_SIZE)
                                               .reader(fileReader)
                                               .processor(processor)
                                               .writer(jdbcBatchWriter)
                                               .taskExecutor(taskExecutor)
                                               .throttleLimit(THROTTLE_LIMIT)
                                               .build();
    }

    public static <T> JdbcBatchItemWriter<T> getJdbcBatchWriter(final DataSource dataSource, final String query) {
        var itemSqlParameterSourceProvider = new BeanPropertyItemSqlParameterSourceProvider<T>();
        var batchItemWriter = new JdbcBatchItemWriterBuilder<T>().itemSqlParameterSourceProvider(itemSqlParameterSourceProvider)
                                                                 .sql(query)
                                                                 .dataSource(dataSource)
                                                                 .build();
        batchItemWriter.afterPropertiesSet();
        return batchItemWriter;
    }

    public static <T> FlatFileItemReader<T> getFileReader(final FieldSetMapper<T> fieldSetMapper, final String path) {
        var lineMapper = getDefaultLineMapper(fieldSetMapper);
        var file = new FileSystemResource(path);
        var recordSeparatorPolicy = new DefaultRecordSeparatorPolicy();
        return getFlatFileItemReader(lineMapper, file, recordSeparatorPolicy);
    }

    public static <T> FlatFileItemReader<T> getFlatFileItemReader(final DefaultLineMapper<T> lineMapper,
                                                                  final FileSystemResource file,
                                                                  final DefaultRecordSeparatorPolicy recordSeparatorPolicy) {
        var reader = new FlatFileItemReader<T>();
        reader.setResource(file);
        reader.setLineMapper(lineMapper);
        reader.setRecordSeparatorPolicy(recordSeparatorPolicy);
        return reader;
    }

    public static <T> DefaultLineMapper<T> getDefaultLineMapper(final FieldSetMapper<T> fieldSetMapper) {
        var lineMapper = new DefaultLineMapper<T>();
        lineMapper.setLineTokenizer(getDelimitedLineTokenizer(FIELD_SEPARATOR_CHARACTER, QUOTE_CHARACTER));
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    public static DelimitedLineTokenizer getDelimitedLineTokenizer(final char delimiter, final char quoteCharacter) {
        var tokenizer = new DelimitedLineTokenizer(String.valueOf(delimiter));
        tokenizer.setQuoteCharacter(quoteCharacter);
        return tokenizer;
    }
}
