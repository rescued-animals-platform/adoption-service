CREATE TABLE large_image (
  id UUID PRIMARY KEY,
  animal_uuid UUID NOT NULL,
  creation_date TIMESTAMP DEFAULT current_timestamp,
  image_type VARCHAR(10) NOT NULL,
  name VARCHAR(100) NOT NULL,
  url VARCHAR(300) NOT NULL UNIQUE,
  CONSTRAINT large_image_animal FOREIGN KEY(animal_uuid) REFERENCES animal(uuid)
);