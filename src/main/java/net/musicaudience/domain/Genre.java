package net.musicaudience.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    private Long id;
    private Long parentId;
    private String name;
}
