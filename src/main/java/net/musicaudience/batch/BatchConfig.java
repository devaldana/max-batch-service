package net.musicaudience.batch;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.context.annotation.*;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;

import static net.musicaudience.Constants.*;
import static net.musicaudience.batch.utils.BatchUtils.*;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job importLogsJob() {
        return jobBuilderFactory.get("maxMusicDataImportJob")
                                .incrementer(new RunIdIncrementer())
                                .start(loadArtistsStep())
                                .build();
    }

    private Step loadArtistsStep() {
        var stepName = "loadArtists";
        var filePath = "C:\\Users\\David\\Documents\\max\\artist";
        var fieldSetMapper = new ArtistMapper();
        var query = "INSERT INTO artists (id, type_id, name, actual, url) VALUES(:id, :typeId, :name, :actual, :url)";
        return buildStep(stepName, filePath, fieldSetMapper, query);
    }

    private <T> Step buildStep(final String stepName, final String filePath, final FieldSetMapper<T> fieldSetMapper, final String query) {
        var fileReader = getFileReader(fieldSetMapper, filePath);
        var jdbcBatchWriter = getJdbcBatchWriter(dataSource, query);
        var taskExecutor = new SimpleAsyncTaskExecutor();
        return stepBuilderFactory.get(stepName).<T, T>chunk(DEFAULT_CHUNK_SIZE)
                                               .reader(fileReader)
                                               .writer(jdbcBatchWriter)
                                               .taskExecutor(taskExecutor)
                                               .throttleLimit(THROTTLE_LIMIT)
                                               .build();
    }
}