CREATE TABLE friendly_with (
  characteristics_uuid UUID NOT NULL,
  friendly_with VARCHAR(14) NOT NULL,
  PRIMARY KEY(characteristics_uuid, friendly_with),
  CONSTRAINT friendly_with_characteristics FOREIGN KEY(characteristics_uuid) REFERENCES characteristics(uuid)
);