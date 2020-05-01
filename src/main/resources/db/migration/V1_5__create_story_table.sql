CREATE TABLE story (
  id UUID PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  animal_id UUID NOT NULL UNIQUE,
  text TEXT NOT NULL,
  CONSTRAINT story_animal FOREIGN KEY(animal_id) REFERENCES animal(id)
);