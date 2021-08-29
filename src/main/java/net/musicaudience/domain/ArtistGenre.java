package net.musicaudience.domain;

import lombok.*;

@Data
@Builder
public class ArtistGenre {
    private Long artistId;
    private Long genreId;
    private boolean primary;
}
