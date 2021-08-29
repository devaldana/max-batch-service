package net.musicaudience.batch;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.musicaudience.domain.Artist;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.*;
import org.springframework.core.io.*;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.musicaudience.Constants.*;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job importLogsJob() {
        return jobBuilderFactory.get("importLogsJob")
                                .incrementer(new RunIdIncrementer())
                                .start(
                                        stepBuilderFactory.get("loadAllRequestsToDatabase")
                                                .<Artist, Artist>chunk(10000)
                                                .reader(reader())
                                                .writer(
                                                        (items) -> items.forEach(blockedIp -> log.info(blockedIp.toString()))
                                                )
                                                .taskExecutor(new SimpleAsyncTaskExecutor("spring_batch"))
                                                .throttleLimit(10)
                                                .build()
                                )
                                .listener(jobExecutionListener())
                                .build();
    }

    @Bean
    public FlatFileItemReader<Artist> reader() {
        var lineMapper = new DefaultLineMapper<Artist>();
        var tokenizer = new DelimitedLineTokenizer(String.valueOf(FIELD_SEPARATOR_CHARACTER));
        tokenizer.setQuoteCharacter(QUOTE_CHARACTER);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper((new ArtistMapper()));

        var reader = new FlatFileItemReader<Artist>();
        reader.setResource(new ClassPathResource("artist"));
        reader.setLineMapper(lineMapper);
        reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        return reader;
    }

    private JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            public void beforeJob(final JobExecution jobExecution) {
                log.info("Starting job with ID: {}", jobExecution.getJobId());
            }

            public void afterJob(final JobExecution jobExecution) {
                var start = jobExecution.getCreateTime().getTime();
                var totalMillis = jobExecution.getEndTime().getTime() - start;
                var totalSeconds = MILLISECONDS.toSeconds(totalMillis);
                log.info("Job total execution time: {}s", totalSeconds);
            }
        };
    }
}