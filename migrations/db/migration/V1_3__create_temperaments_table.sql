CREATE TABLE temperaments (
  characteristics_id BIGSERIAL,
  temperament VARCHAR(35) NOT NULL,
  PRIMARY KEY(characteristics_id, temperament),
  CONSTRAINT temperaments_characteristics FOREIGN KEY(characteristics_id) REFERENCES characteristics(id)
);