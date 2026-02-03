INSERT INTO product (id, sku, name, price, category, discount) VALUES (1, 'p-010', 'Mouse', 50.00, 'Electronics', 0.00);
INSERT INTO product (id, sku, name, price, category, discount) VALUES (2, 'p-001', 'Laptop', 1000.00, 'Electronics', 20.00);
INSERT INTO product (id, sku, name, price, category, discount) VALUES (3, 'p-003', 'Keyboard', 80.00, 'Electronics', 0.00);

INSERT INTO payment_method (id, name, type, discount) VALUES (2, 'Debit Card', 'DEBIT', 0.10);
INSERT INTO payment_method (id, name, type, discount) VALUES (3, 'Credit Card', 'CREDIT_CARD', 0.05);

INSERT INTO promotion (id, name, strategy_type, config_value, active) VALUES (1, 'Global Summer Sale', 'TEN_PERCENT_OFF', 10.00, 'N');
