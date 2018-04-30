CREATE TABLE animal_for_adoption (
  uuid varchar(40) CONSTRAINT primarykey PRIMARY KEY,
  name varchar(40) NOT NULL,
  registration_date timestamp DEFAULT current_timestamp
);