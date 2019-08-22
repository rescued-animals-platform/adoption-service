CREATE TABLE primary_link_picture (
  id BIGSERIAL PRIMARY KEY,
  creation_date TIMESTAMP DEFAULT current_timestamp,
  animal_uuid UUID NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL,
  large_image_url VARCHAR(300),
  small_image_url VARCHAR(300),
  CONSTRAINT primary_link_picture_animal FOREIGN KEY(animal_uuid) REFERENCES animal(uuid)
);