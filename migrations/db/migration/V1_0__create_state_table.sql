CREATE TABLE state (
  id UUID PRIMARY KEY,
  state_name VARCHAR(20) NOT NULL,
  state JSONB NOT NULL
);

