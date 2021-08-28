package net.musicaudience.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "artist_genres")
public class ArtistGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
