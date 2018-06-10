CREATE TABLE media_link (
  animal_uuid UUID NOT NULL,
  media_name VARCHAR(50) NOT NULL,
  creation_date TIMESTAMP DEFAULT current_timestamp,
  url VARCHAR(300) NOT NULL UNIQUE,
  PRIMARY KEY(animal_uuid, media_name),
  CONSTRAINT media_link_animal FOREIGN KEY(animal_uuid) REFERENCES animal(uuid)
);