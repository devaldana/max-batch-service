package net.musicaudience.config;

import lombok.RequiredArgsConstructor;
import net.musicaudience.util.ArgumentsData;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.io.File;

import static net.musicaudience.util.Constants.*;

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
}
