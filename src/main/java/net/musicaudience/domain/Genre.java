package net.musicaudience.domain;

import lombok.Data;

@Data
public class Genre {
    private Long id;
    private Long parentId;
    private String name;
}
