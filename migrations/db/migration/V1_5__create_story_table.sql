CREATE TABLE story (
  uuid UUID PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  animal_uuid UUID NOT NULL UNIQUE,
  text TEXT NOT NULL,
  CONSTRAINT story_animal FOREIGN KEY(animal_uuid) REFERENCES animal(uuid)
);