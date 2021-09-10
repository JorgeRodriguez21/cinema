ALTER TABLE movie
    ADD CONSTRAINT unique_imdb UNIQUE (imdb_id);