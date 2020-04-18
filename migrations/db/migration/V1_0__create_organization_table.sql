CREATE TABLE organization (
  uuid UUID PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  city VARCHAR(50) NOT NULL,
  reception_address VARCHAR(300) NOT NULL,
  email VARCHAR(50) NOT NULL,
  adoption_form_pdf_url VARCHAR(300) NOT NULL
);