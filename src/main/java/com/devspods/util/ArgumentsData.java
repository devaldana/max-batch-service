package com.devspods.util;

import lombok.*;

@Data
@Builder
public class ArgumentsData {
    private String artistsFilePath;
    private String genresFilePath;
    private String artistsGenresFilePath;
}
