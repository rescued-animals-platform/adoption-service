CREATE TABLE primary_link_picture (
  id UUID PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  animal_id UUID NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL,
  large_image_url VARCHAR(300),
  small_image_url VARCHAR(300),
  CONSTRAINT primary_link_picture_animal FOREIGN KEY(animal_id) REFERENCES animal(id)
);