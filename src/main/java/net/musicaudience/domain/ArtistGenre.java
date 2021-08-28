package net.musicaudience.domain;

import lombok.Data;

@Data
public class ArtistGenre {
    private Long artistId;
    private Long genreId;
    private boolean isPrimary;
}
