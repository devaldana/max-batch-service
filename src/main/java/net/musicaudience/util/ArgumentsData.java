package net.musicaudience.util;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class ArgumentsData {
    private String artistsFilePath;
    private String genresFilePath;
    private String artistsGenresFilePath;
}
