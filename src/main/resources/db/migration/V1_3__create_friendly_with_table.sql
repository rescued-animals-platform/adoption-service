CREATE TABLE friendly_with (
  characteristics_id UUID NOT NULL,
  friendly_with VARCHAR(14) NOT NULL,
  PRIMARY KEY(characteristics_id, friendly_with),
  CONSTRAINT friendly_with_characteristics FOREIGN KEY(characteristics_id) REFERENCES characteristics(id)
);