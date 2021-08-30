package com.devspods.domain;

import lombok.*;

@Data
@Builder
public class Artist {
    private Long id;
    private boolean actual;
    private String name;
    private Long typeId;
    private String url;
}
