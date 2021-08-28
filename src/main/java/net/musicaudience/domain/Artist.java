package net.musicaudience.domain;

import lombok.Data;

@Data
public class Artist {
    private Long id;
    private boolean isActual;
    private String name;
    private Long typeId;
    private String url;
}
