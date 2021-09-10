CREATE TABLE IF NOT EXISTS movie (
    id serial NOT NULL,
    title varchar NOT NULL,
    imdb_id varchar NOT NULL,
    CONSTRAINT movie_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "user" (
  id serial NOT NULL,
  "role" varchar NOT NULL,
   username varchar NOT NULL,
   password varchar NOT NULL,
  CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS room (
  id serial NOT NULL,
  "type" varchar NOT NULL,
  CONSTRAINT room_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS movie_room (
  movie_id INT NOT NULL,
  room_id INT NOT NULL,
  price decimal NOT NULL,
  "time" varchar NOT NULL,
  CONSTRAINT movie_room_pk PRIMARY KEY (movie_id,room_id),
  FOREIGN KEY (movie_id) references movie (id),
  FOREIGN KEY (room_id) references room (id)
);

CREATE TABLE IF NOT EXISTS review (
  id serial NOT NULL,
  rate decimal NOT NULL,
  comment varchar,
  movie_id INT NOT NULL,
  user_id INT NOT NULL,
  CONSTRAINT review_pk PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES "user"(id),
  FOREIGN KEY (movie_id) REFERENCES movie(id)
);

CREATE TABLE IF NOT EXISTS reservation(
  id serial NOT NULL,
  ticket_number INT NOT NULL,
  user_id INT NOT NULL,
  movie_id INT NOT NULL,
  room_id INT NOT NULL,
  CONSTRAINT reservation_pk PRIMARY KEY (id),
  FOREIGN KEY (movie_id, room_id) REFERENCES movie_room(movie_id,room_id),
  FOREIGN KEY (user_id) REFERENCES "user"(id)
);