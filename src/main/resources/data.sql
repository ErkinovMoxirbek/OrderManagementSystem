INSERT INTO products (name, price, stock, category, is_active, created_at)
VALUES ('Piyoz', 1500.0, 20, 'sabzovot', true, CURRENT_TIMESTAMP);
INSERT INTO products (name, price, stock, category, is_active, created_at)
VALUES ('Kartoshka', 2500.0, 25, 'sabzovot', true, CURRENT_TIMESTAMP);
INSERT INTO products (name, price, stock, category, is_active, created_at)
VALUES ('Pepsi 1L', 4000.0, 25, 'suv', true, CURRENT_TIMESTAMP);
INSERT INTO products (name, price, stock, category, is_active, created_at)
VALUES ('Fanta 1L', 4000.0, 25, 'suv', true, CURRENT_TIMESTAMP);
INSERT INTO profile (id, full_name, email, password, status, role, visible, created_date)
VALUES
    ('1e9b4f30-1234-4567-890a-abcdef123456', 'John Doe', 'doe@example.com', '123456', 'ACTIVE', 'CUSTOMER', true, NOW()),

    ('2a7c8e20-2345-5678-901b-bcdefa234567', 'Example Example', 'example@example.com', '123456', 'ACTIVE', 'ADMIN', true, NOW()),

    ('3b8d9f10-3456-6789-012c-cdefab345678', 'Bob Johnson', 'bob@mail.uz', '123456', 'NOACTIVE', 'CUSTOMER', false, NOW());
