CREATE TABLE organization (
  id UUID PRIMARY KEY,
  registration_date TIMESTAMP DEFAULT current_timestamp,
  last_updated_at TIMESTAMP DEFAULT current_timestamp,
  name VARCHAR(100) NOT NULL,
  city VARCHAR(50) NOT NULL,
  reception_address VARCHAR(300) NOT NULL,
  email VARCHAR(50) NOT NULL,
  adoption_form_pdf_url VARCHAR(300) NOT NULL
);