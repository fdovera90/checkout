INSERT INTO product (id, sku, name, price, category, discount, image_url) VALUES (1, 'p-010', 'Mouse', 50.00, 'Electronics', 0.00, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?q=80&w=200');
INSERT INTO product (id, sku, name, price, category, discount, image_url) VALUES (2, 'p-001', 'Laptop', 1000.00, 'Electronics', 20.00, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?q=80&w=200');
INSERT INTO product (id, sku, name, price, category, discount, image_url) VALUES (3, 'p-003', 'Keyboard', 80.00, 'Electronics', 0.00, 'https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?q=80&w=200');

INSERT INTO payment_method (id, name, type, discount) VALUES (1, 'Debit Card', 'DEBIT', 0.10);
INSERT INTO payment_method (id, name, type, discount) VALUES (2, 'Credit Card', 'CREDIT_CARD', 0.05);

INSERT INTO promotion (id, name, strategy_type, config_value, active) VALUES (1, 'Global Summer Sale', 'TEN_PERCENT_OFF', 10.00, 'S');

INSERT INTO shipping_zone (id, name, cost) VALUES ('zone-1', 'Santiago Centro', 10.00);
INSERT INTO shipping_zone (id, name, cost) VALUES ('zone-2', 'Santiago Periferia', 25.00);
INSERT INTO shipping_zone (id, name, cost) VALUES ('zone-3', 'Regiones Cercanas', 50.00);
INSERT INTO shipping_zone (id, name, cost) VALUES ('zone-4', 'Regiones Extremas', 100.00);
INSERT INTO shipping_zone (id, name, cost) VALUES ('zone-5', 'Isla de Pascua / Juan Fernández', 150.00);
