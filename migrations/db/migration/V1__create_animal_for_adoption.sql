CREATE TABLE animal_for_adoption (
  id BIGSERIAL CONSTRAINT primarykey PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  uuid VARCHAR(40) NOT NULL,
  name VARCHAR(40) NOT NULL,
  type VARCHAR(3) NOT NULL,
  CONSTRAINT uniqueuuid UNIQUE(uuid)
);