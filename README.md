# Quick start guide
This guide assumes that you have Docker installed. Execute the `start.sh` or the `start.bat` depending on whether you use Linux/Mac or Windows.
Note that for Linux/Mac users one extra step is required: you have to give execution permissions to `start.sh`, to achieve that, execute `sudo chmod +x start.sh`.

### Linux/Mac execution example
```
> sudo chmod +x start.sh
> sh start.sh /Users/David/projects/max-batch-service/data
```

### Windows execution example
```
> start.bat C:\Users\David\projects\max-batch-service\data
```

Note that the script receives one parameter, it's the path of the folder containing the required files: `artist`, `genre` and `genre_artist`.
This script compiles, packages and executes the application which turn will use the given folder path to load those records to database.

### Sample queries
```
-- Give me all the artists
SELECT * FROM artists;

-- Give me all the genres
SELECT * FROM genres;

-- Give me all the non-actual artists
SELECT * FROM artists
WHERE actual IS FALSE;

-- Give me numbers of the artists which have Pop (ID=14) as secondary genre
SELECT count(a.id)
FROM artists a
INNER JOIN artists_genres ag ON ag.artist_id = a.id
WHERE ag.is_primary IS FALSE AND ag.genre_id = 14;

-- Give me all the actual artists with their primary music genre ordered by genre name
SELECT g.name AS genre, a.name AS artist
FROM artists a
INNER JOIN artists_genres ag ON ag.artist_id = a.id
INNER JOIN genres g ON g.id = ag.genre_id
WHERE ag.is_primary AND a.actual
ORDER BY g.name;

-- Give me the number of artists per each genre where the number of artist is greater than or equal to 100
-- exclude duplicated records from each genre and order descending by count.
SELECT g.name, COUNT(ag.artist_id)
FROM artists_genres ag 
INNER JOIN genres g ON g.id = ag.genre_id AND ag.is_primary
GROUP BY (ag.genre_id)
HAVING COUNT(ag.artist_id) >= 100
ORDER BY COUNT(ag.artist_id) DESC;
```

### System design - some details
This application was developed using Java programming language and [Spring Batch](https://spring.io/projects/spring-batch), which is:
> A lightweight, comprehensive batch framework designed to enable the development of robust batch applications vital for the daily operations of enterprise systems.

This application loads the data to database in three steps: _loadArtists_, _loadGenres_ and _loadArtistsGenres_.
Each step has a _reader_, a _mapper_, a _processor_ and a _writer_. For each step a chunk size can be defined and each chunk of data is processed in
a different _thread_. Write to database is done in batches of the same chunk size.

You may guess that we can run _loadArtists_ and _loadGenres_ in parallel and then continue with _loadArtistsGenres_, you are right, however,
for the sake of simplicity and considering that they will be executed on the same node/computer/processor, and it's compute/IO intensive operation,
we're running the steps in sequential mode.

It was developed in a modular way so adding new steps is straightforward.

### Things to improve
* Unfortunately I was not able to add unit/integration tests because of lack of time, so, tests are required to be implemented.
* More comments explaining some parts of the code should be added - I will explain everything on our code review.
* We should add more logs for debugging purpose.
* Code documentation is also pending, so, it should be added.
* Probably it would be a good idea to add indexes for some columns.
* I don't like too much the way the data-integrity-violation-exceptions are handled, using some record of the already processed artists and genres might not be enough.
* Would be nice to have another table for recording the imported data meta-data.
* This document itself need to be improved to explain more technical decisions and the way the project works behind the scenes.

https://devspods.com/