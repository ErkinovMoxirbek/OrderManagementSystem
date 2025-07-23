CREATE TABLE IF NOT EXISTS products
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    category VARCHAR
(
    255
),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS orders
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    profile_id
    VARCHAR
(
    255
) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_status VARCHAR
(
    50
) DEFAULT 'PENDING',
    total_amount DOUBLE NOT NULL
    );

CREATE TABLE IF NOT EXISTS order_items
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    order_id
    BIGINT
    NOT
    NULL,
    product_id
    BIGINT
    NOT
    NULL,
    quantity
    INT
    NOT
    NULL,
    price
    DOUBLE,
    FOREIGN
    KEY
(
    order_id
) REFERENCES orders
(
    id
) ON DELETE CASCADE,
    FOREIGN KEY
(
    product_id
) REFERENCES products
(
    id
)
  ON DELETE CASCADE
    );

create table profile
(
    id           varchar(36) primary key,
    full_name    varchar(255),
    email        varchar(255) NOT NULL unique,
    password     varchar(255),
    status       varchar(50),
    role         varchar(50),
    visible      boolean default true,
    created_date timestamp,
    verified     boolean default false
);
CREATE TABLE email_history
(
    id           VARCHAR(36) PRIMARY KEY,
    to_account   VARCHAR(255),
    body         TEXT,
    unique_info VARCHAR (255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
