CREATE TABLE animal (
  id BIGSERIAL PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  uuid VARCHAR(40) NOT NULL UNIQUE,
  name VARCHAR(40) NOT NULL,
  type VARCHAR(3) NOT NULL,
  estimated_age VARCHAR(12) NOT NULL,
  state_id BIGSERIAL NOT NULL,
  CONSTRAINT animal_state FOREIGN KEY(state_id) REFERENCES state(id)
);