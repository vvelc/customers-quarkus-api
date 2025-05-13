CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    second_name VARCHAR(100),
    first_last_name VARCHAR(100) NOT NULL,
    second_last_name VARCHAR(100),
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    demonym VARCHAR(100) NOT NULL,
    CONSTRAINT uk_customer_email UNIQUE (email)
);

CREATE INDEX idx_customer_country ON customers(country);
CREATE INDEX idx_customer_email ON customers(email);
