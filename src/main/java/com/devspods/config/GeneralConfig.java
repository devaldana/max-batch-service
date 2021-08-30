package com.devspods.config;

import com.devspods.util.ArgumentsData;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.support.DatabaseStartupValidator;

import javax.sql.DataSource;
import java.io.File;

import static com.devspods.util.Constants.*;

@Configuration
@RequiredArgsConstructor
public class GeneralConfig {

    private final Environment env;

    @Bean
    public ArgumentsData argumentsData() {
        var filesLocation = env.getProperty(FILES_FOLDER_PATH_ARG) + File.separator;
        return ArgumentsData.builder()
                            .artistsFilePath(filesLocation + ARTISTS_FILE_NAME)
                            .genresFilePath(filesLocation + GENRES_FILE_NAME)
                            .artistsGenresFilePath(filesLocation + ARTISTS_GENRES_FILE_NAME)
                            .build();
    }

    @Bean
    public DatabaseStartupValidator databaseStartupValidator(final DataSource dataSource) {
        var dsv = new DatabaseStartupValidator();
        dsv.setDataSource(dataSource);
        return dsv;
    }
}
