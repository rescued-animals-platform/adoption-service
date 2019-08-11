CREATE TABLE animal (
  uuid UUID PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  clinical_record VARCHAR(40) NOT NULL UNIQUE,
  name VARCHAR(40) NOT NULL,
  animal_species VARCHAR(3) NOT NULL,
  estimated_age VARCHAR(12) NOT NULL,
  sex VARCHAR(6) NOT NULL,
  state_name VARCHAR(20) NOT NULL,
  state TEXT NOT NULL
);