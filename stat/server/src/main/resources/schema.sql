CREATE TABLE IF NOT EXISTS stats(
stat_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
app VARCHAR(200),
uri VARCHAR(200),
ip VARCHAR(15),
created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);