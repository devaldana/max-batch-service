package net.musicaudience.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "artists")
public class Artist {
    @Id
    private Long id;
    private Long typeId;
    private boolean isActual;

    @Column(length = 1000)
    private String name;

    @Column(length = 1000)
    private String url;
}
