CREATE TABLE characteristics (
  uuid UUID PRIMARY KEY,
  animal_uuid UUID NOT NULL UNIQUE,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  size VARCHAR(7) NOT NULL,
  physical_activity VARCHAR(6) NOT NULL,
  sociability VARCHAR(24),
  docility VARCHAR(27),
  balance VARCHAR(31),
  CONSTRAINT characteristics_animal FOREIGN KEY(animal_uuid) REFERENCES animal(uuid)
);