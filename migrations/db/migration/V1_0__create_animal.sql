CREATE TABLE animal (
  id BIGSERIAL CONSTRAINT primarykey PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  uuid VARCHAR(40) NOT NULL,
  name VARCHAR(40) NOT NULL,
  type VARCHAR(3) NOT NULL,
  estimated_age VARCHAR(12) NOT NULL,
  CONSTRAINT uniqueuuid UNIQUE(uuid)
);