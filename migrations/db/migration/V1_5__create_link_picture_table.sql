CREATE TABLE link_picture (
  id BIGSERIAL PRIMARY KEY,
  creation_date TIMESTAMP DEFAULT current_timestamp,
  animal_uuid UUID NOT NULL,
  name VARCHAR(50) NOT NULL,
  picture_type VARCHAR(10) NOT NULL,
  large_image_url VARCHAR(300) NOT NULL UNIQUE,
  small_image_url VARCHAR(300) NOT NULL UNIQUE,
  CONSTRAINT link_picture_animal FOREIGN KEY(animal_uuid) REFERENCES animal(uuid)
);