CREATE TABLE animal (
  id UUID PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  last_updated_at TIMESTAMP DEFAULT current_timestamp,
  clinical_record VARCHAR(40) NOT NULL,
  name VARCHAR(40),
  species VARCHAR(3) NOT NULL,
  estimated_age VARCHAR(12) NOT NULL,
  sex VARCHAR(6) NOT NULL,
  state_name VARCHAR(20) NOT NULL,
  adoption_form_id VARCHAR(100),
  unavailable_state_notes TEXT,
  organization_id UUID NOT NULL,
  CONSTRAINT animal_organization FOREIGN KEY(organization_id) REFERENCES organization(id)
);