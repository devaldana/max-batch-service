package com.devspods.config;

import com.devspods.util.*;
import lombok.RequiredArgsConstructor;
import com.devspods.util.ArgumentsData;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class GeneralConfig {

    private final Environment env;

    @Bean
    public ArgumentsData argumentsData() {
        var filesLocation = env.getProperty(Constants.FILES_FOLDER_PATH_ARG) + File.separator;
        return ArgumentsData.builder()
                            .artistsFilePath(filesLocation + Constants.ARTISTS_FILE_NAME)
                            .genresFilePath(filesLocation + Constants.GENRES_FILE_NAME)
                            .artistsGenresFilePath(filesLocation + Constants.ARTISTS_GENRES_FILE_NAME)
                            .build();
    }
}
