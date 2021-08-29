package net.musicaudience.batch;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.musicaudience.batch.mappers.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.context.annotation.*;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.dao.DataIntegrityViolationException;

import javax.sql.DataSource;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static net.musicaudience.Constants.*;
import static net.musicaudience.batch.utils.BatchUtils.*;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private Set<Long> importedArtists = ConcurrentHashMap.newKeySet();
    private Set<Long> importedGenres = ConcurrentHashMap.newKeySet();

    @Bean
    public Job importLogsJob() {
        return jobBuilderFactory.get("maxMusicDataImportJob")
                                .incrementer(new RunIdIncrementer())
                                .start(loadArtistsStep())
                                .next(loadGenresStep())
                                .next(loadArtistsGenresStep())
                                .build();
    }

    private Step loadArtistsStep() {
        var stepName = "loadArtists";
        var filePath = "C:\\Users\\David\\Documents\\max\\artist.min";
        var fieldSetMapper = new ArtistMapper();
        var query = "INSERT INTO artists (id, type_id, name, actual, url) VALUES(:id, :typeId, :name, :actual, :url)";
        return buildStep(stepName, filePath, fieldSetMapper, query);
    }

    private Step loadGenresStep() {
        var stepName = "loadGenres";
        var filePath = "C:\\Users\\David\\Documents\\max\\genre.min";
        var fieldSetMapper = new GenreMapper();
        var query = "INSERT INTO genres (id, parent_id, name) VALUES(:id, :parentId, :name)";
        return buildStep(stepName, filePath, fieldSetMapper, query);
    }

    private Step loadArtistsGenresStep() {
        var stepName = "loadArtistsGenres";
        var filePath = "C:\\Users\\David\\Documents\\max\\genre_artist.min";
        var fieldSetMapper = new ArtistGenreMapper();
        var query = "INSERT INTO artists_genres (artist_id, genre_id, is_primary) VALUES(:artistId, :genreId, :primary)";
        return buildStep(stepName, filePath, fieldSetMapper, query);
    }

    private <T> Step buildStep(final String stepName, final String filePath, final FieldSetMapper<T> fieldSetMapper, final String query) {
        var fileReader = getFileReader(fieldSetMapper, filePath);
        var jdbcBatchWriter = getJdbcBatchWriter(dataSource, query);
        var taskExecutor = new SimpleAsyncTaskExecutor();
        return stepBuilderFactory.get(stepName).<T, T>chunk(DEFAULT_CHUNK_SIZE)
                                               .reader(fileReader)
                                               .writer(jdbcBatchWriter)
                                               .faultTolerant()
                                               .skip(DataIntegrityViolationException.class)
                                               .skipLimit(Integer.MAX_VALUE)
                                               .taskExecutor(taskExecutor)
                                               .throttleLimit(THROTTLE_LIMIT)
                                               .build();
    }
}