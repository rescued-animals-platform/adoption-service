CREATE TABLE small_image (
  id UUID PRIMARY KEY,
  large_image_id UUID NOT NULL,
  creation_date TIMESTAMP DEFAULT current_timestamp,
  url VARCHAR(300) NOT NULL UNIQUE,
  CONSTRAINT small_image_large_image FOREIGN KEY(large_image_id) REFERENCES large_image(id)
);