package net.musicaudience.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.musicaudience.batch.mappers.*;
import net.musicaudience.batch.utils.BatchUtils;
import net.musicaudience.domain.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final Set<Long> importedArtists = ConcurrentHashMap.newKeySet();
    private final Set<Long> importedGenres = ConcurrentHashMap.newKeySet();

    @Bean
    public Job musicDataImportJob() {
        return jobBuilderFactory.get("maxMusicDataImportJob")
                                .incrementer(new RunIdIncrementer())
                                .start(loadArtistsStep())
                                .next(loadGenresStep())
                                .next(loadArtistsGenresStep())
                                .build();
    }

    private Step loadArtistsStep() {
        var stepName = "loadArtists";
        var filePath = "C:\\Users\\David\\Documents\\max\\artist";
        var fieldSetMapper = new ArtistMapper();
        var query = "INSERT INTO artists (id, type_id, name, actual, url) VALUES(:id, :typeId, :name, :actual, :url)";
        ItemProcessor<Artist, Artist> processor = artist -> {
            importedArtists.add(artist.getId());
            return artist;
        };
        return buildStep(stepName, filePath, fieldSetMapper, query, processor);
    }

    private Step loadGenresStep() {
        var stepName = "loadGenres";
        var filePath = "C:\\Users\\David\\Documents\\max\\genre";
        var fieldSetMapper = new GenreMapper();
        var query = "INSERT INTO genres (id, parent_id, name) VALUES(:id, :parentId, :name)";
        ItemProcessor<Genre, Genre> processor = genre -> {
            importedGenres.add(genre.getId());
            return genre;
        };
        return buildStep(stepName, filePath, fieldSetMapper, query, processor);
    }

    private Step loadArtistsGenresStep() {
        var stepName = "loadArtistsGenres";
        var filePath = "C:\\Users\\David\\Documents\\max\\genre_artist";
        var fieldSetMapper = new ArtistGenreMapper();
        var query = "INSERT INTO artists_genres (artist_id, genre_id, is_primary) VALUES(:artistId, :genreId, :primary)";
        ItemProcessor<ArtistGenre, ArtistGenre> processor = artistGenre -> {
            if(!importedArtists.contains(artistGenre.getArtistId()) || !importedGenres.contains(artistGenre.getGenreId())) {
                return null;
            }
            return artistGenre;
        };
        return buildStep(stepName, filePath, fieldSetMapper, query, processor);
    }

    private <T> Step buildStep(final String stepName,
                               final String filePath,
                               final FieldSetMapper<T> fieldSetMapper,
                               final String query,
                               final ItemProcessor<T, T> processor) {
        return BatchUtils.buildStep(stepName, filePath, fieldSetMapper, query, processor, stepBuilderFactory, dataSource);
    }
}