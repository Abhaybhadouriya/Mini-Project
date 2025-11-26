-- schema.sql (trimmed/portable)
CREATE TABLE bills (
  id BIGINT PRIMARY KEY,
  amount DECIMAL(38,2) NOT NULL,
  bill_date DATE NOT NULL,
  deadline DATE NOT NULL,
  description VARCHAR(255) NOT NULL
);

CREATE TABLE students (
  id BIGINT PRIMARY KEY,
  cgpa FLOAT,
  domain VARCHAR(255),
  email VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  graduation_year INT,
  last_name VARCHAR(255),
  password VARCHAR(255) NOT NULL,
  photograph_path VARCHAR(255),
  placement_id INT,
  roll_number VARCHAR(255) NOT NULL,
  specialisation VARCHAR(255),
  total_credits FLOAT,
  UNIQUE (email),
  UNIQUE (roll_number)
);

CREATE TABLE student_payment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  amount DECIMAL(38,2) NOT NULL,
  description VARCHAR(255),
  payment_date DATE NOT NULL,
  bill_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL
);

-- add other tables similarly...
