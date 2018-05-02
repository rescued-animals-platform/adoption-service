CREATE TABLE animal_for_adoption (
  id BIGSERIAL CONSTRAINT primarykey PRIMARY KEY,
  uuid VARCHAR(40) NOT NULL,
  name VARCHAR(40) NOT NULL,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  CONSTRAINT uniqueuuid UNIQUE(uuid)
);