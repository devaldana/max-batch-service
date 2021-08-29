DROP TABLE IF EXISTS artists_genres CASCADE;
DROP TABLE IF EXISTS artists CASCADE;
DROP TABLE IF EXISTS genres CASCADE;

CREATE TABLE artists (
    id      BIGINT NOT NULL PRIMARY KEY,
    type_id BIGINT NOT NULL,
    name    VARCHAR(1000),
    actual  TINYINT(1),
    url     VARCHAR(1000)
);

CREATE TABLE genres (
    id        BIGINT NOT NULL PRIMARY KEY,
    parent_id BIGINT NOT NULL,
    name      VARCHAR(200)
);

CREATE TABLE artists_genres (
    artist_id  BIGINT NOT NULL,
    genre_id   BIGINT NOT NULL,
    is_primary TINYINT(1),

    PRIMARY KEY (artist_id, genre_id),
    CONSTRAINT fk_to_artists FOREIGN KEY (artist_id) REFERENCES artists (id) ON DELETE CASCADE,
    CONSTRAINT fk_to_genres  FOREIGN KEY (genre_id)  REFERENCES genres (id)  ON DELETE CASCADE
);