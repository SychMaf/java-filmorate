# java-filmorate
*some project description*


# Database design

![database model](src/main/resources/BD.png)

## Example of queries for accessing the DB:

Get full information about films (FilmController method: *findAll*):
``` SQL
SELECT f.film_id,
	f.name,
        f.description,
        f.releasedate,
        f.duration,
        f.rating,
        gr.genre
FROM film AS f
LEFT OUTER JOIN film_genre AS fg ON f.film_id = fg.film_id
LEFT OUTER JOIN genre AS gr ON fg.genre_id = gr.genre_id
```

Get full(*except friends*) information about users (UserController method: *findAll*):
``` SQL
SELECT *
FROM client
```

Get users friends (UserService method: *getFriends*):
``` SQL
SELECT c.user_id,
        fr.second,
        s.status
FROM client AS c
LEFT OUTER JOIN friendship AS fr ON c.user_id = fr.first
LEFT OUTER JOIN status AS s ON fr.status_id = s.status_id
```

Get amount(*sorting*) of likes for a movie (FilmService method: *getPopularFilms*):
``` SQL
SELECT f.film_id AS id,
        f.name AS name,
        COUNT (l.user_id) AS num
FROM film AS f
LEFT OUTER JOIN likes AS l ON f.film_id = l.film_id
GROUP BY id,
        name
ORDER BY num DESC
LIMIT 10;
```

Get a list of users and movies that they liked:
``` SQL
SELECT c.user_id AS id,
        c.name AS name,
        l.film_id
FROM client AS c
LEFT OUTER JOIN likes AS l ON c.user_id = l.user_id
```