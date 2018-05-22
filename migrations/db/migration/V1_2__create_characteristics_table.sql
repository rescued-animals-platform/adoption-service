CREATE TABLE characteristics (
  id BIGSERIAL PRIMARY KEY,
  animal_uuid UUID NOT NULL UNIQUE,
  creation_date TIMESTAMP DEFAULT current_timestamp,
  size VARCHAR(7) NOT NULL,
  physical_activity VARCHAR(6) NOT NULL,
  CONSTRAINT characteristics_animal FOREIGN KEY(animal_uuid) REFERENCES animal(uuid)
);