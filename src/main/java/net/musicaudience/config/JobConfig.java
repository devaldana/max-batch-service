package net.musicaudience.config;

import lombok.RequiredArgsConstructor;
import net.musicaudience.batch.BatchUtils;
import net.musicaudience.batch.mappers.*;
import net.musicaudience.domain.*;
import net.musicaudience.util.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static net.musicaudience.util.Constants.*;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final ArgumentsData argumentsData;
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
        var filePath = argumentsData.getArtistsFilePath();
        var fieldSetMapper = new ArtistMapper();
        return buildStep(stepName, filePath, fieldSetMapper, INSERT_ARTIST_QUERY, this::artistProcessor);
    }

    private Step loadGenresStep() {
        var stepName = "loadGenres";
        var filePath = argumentsData.getGenresFilePath();
        var fieldSetMapper = new GenreMapper();
        return buildStep(stepName, filePath, fieldSetMapper, INSERT_GENRE_QUERY, this::genreProcessor);
    }

    private Step loadArtistsGenresStep() {
        var stepName = "loadArtistsGenres";
        var filePath = argumentsData.getArtistsGenresFilePath();
        var fieldSetMapper = new ArtistGenreMapper();
        return buildStep(stepName, filePath, fieldSetMapper, INSERT_ARTIST_GENRE_QUERY, this::artistGenreProcessor);
    }

    private Artist artistProcessor(final Artist artist) {
        importedArtists.add(artist.getId());
        return artist;
    }

    private Genre genreProcessor(final Genre genre) {
        importedGenres.add(genre.getId());
        return genre;
    }

    private ArtistGenre artistGenreProcessor(final ArtistGenre artistGenre) {
        if (!importedArtists.contains(artistGenre.getArtistId()) || !importedGenres.contains(artistGenre.getGenreId())) {
            return null;
        }
        return artistGenre;
    }

    private <T> Step buildStep(final String stepName,
                               final String filePath,
                               final FieldSetMapper<T> fieldSetMapper,
                               final String query,
                               final ItemProcessor<T, T> processor) {
        return BatchUtils.buildStep(stepName, filePath, fieldSetMapper, query, processor, stepBuilderFactory, dataSource);
    }
}