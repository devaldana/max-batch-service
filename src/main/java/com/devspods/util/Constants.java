package com.devspods.util;

public final class Constants {
    public static final char FIELD_SEPARATOR_CHARACTER = 1;
    public static final char RECORD_SEPARATOR_CHARACTER = 2;
    public static final char QUOTE_CHARACTER = 3;
    public static final int TRUE = 1;
    public static final int DEFAULT_CHUNK_SIZE = 10000;
    public static final int THROTTLE_LIMIT = 8;
    public static final String FILES_FOLDER_PATH_ARG = "filesLocation";
    public static final String ARTISTS_FILE_NAME = "artist";
    public static final String GENRES_FILE_NAME = "genre";
    public static final String ARTISTS_GENRES_FILE_NAME = "genre_artist";
    public static final String INSERT_ARTIST_QUERY = "INSERT INTO artists (id, type_id, name, actual, url) VALUES(:id, :typeId, :name, :actual, :url)";
    public static final String INSERT_GENRE_QUERY = "INSERT INTO genres (id, parent_id, name) VALUES(:id, :parentId, :name)";
    public static final String INSERT_ARTIST_GENRE_QUERY = "INSERT INTO artists_genres (artist_id, genre_id, is_primary) VALUES(:artistId, :genreId, :primary)";

    private Constants(){}
}
