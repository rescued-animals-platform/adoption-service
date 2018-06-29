CREATE TABLE state (
  id UUID PRIMARY KEY,
  animal_uuid UUID NOT NULL UNIQUE,
  state_name VARCHAR(20) NOT NULL,
  state VARCHAR(100) NOT NULL,
  CONSTRAINT state_animal FOREIGN KEY(animal_uuid) REFERENCES animal(uuid)
);

