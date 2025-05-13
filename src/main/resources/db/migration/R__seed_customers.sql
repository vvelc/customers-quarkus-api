/* R__seed_customers.sql
   10 clientes: 3 US · 3 CA · 4 MX
   Ajusta direcciones y teléfonos a tu gusto.
*/

INSERT INTO customers
(first_name, second_name, first_last_name, second_last_name,
 email, address, phone, country, demonym)
VALUES
-- ---------- EE. UU. (3) ----------
('Alice',  'Marie',    'Walker',  NULL,
 'alice.walker@example.com',
 '123 Maple St, Springfield, IL, 62704',
 '+1-217-555-0101',
 'US', 'American'),

('Brian',  NULL,       'Adams',   'Smith',
 'brian.adams@example.com',
 '456 Oak Ave, Austin, TX, 73301',
 '+1-512-555-0199',
 'US', 'American'),

('Cathy',  'Ann',      'Miller',  NULL,
 'cathy.miller@example.com',
 '789 Pine Rd, Denver, CO, 80202',
 '+1-303-555-0177',
 'US', 'American'),

-- ---------- Canadá (3) ----------
('David',  'James',    'Brown',   NULL,
 'david.brown@example.ca',
 '101 Birch Ln, Toronto, ON, M5H 2N2',
 '+1-416-555-0123',
 'CA', 'Canadian'),

('Ella',   NULL,       'Wilson',  'Taylor',
 'ella.wilson@example.ca',
 '202 Cedar Dr, Vancouver, BC, V6B 1A1',
 '+1-604-555-0456',
 'CA', 'Canadian'),

('Frank',  'Lee',      'Young',   NULL,
 'frank.young@example.ca',
 '303 Spruce Pl, Calgary, AB, T2P 1J9',
 '+1-403-555-0789',
 'CA', 'Canadian'),

-- ---------- México (4) ----------
('Gabriela', 'María',  'López',   'Hernández',
 'gabriela.lopez@example.mx',
 'Av. Reforma 100, CDMX, 06600',
 '+52-55-5555-1010',
 'MX', 'Mexican'),

('Héctor',  NULL,      'Ramírez', 'Díaz',
 'hector.ramirez@example.mx',
 'Calle 10 #200, Guadalajara, JAL, 44100',
 '+52-33-5555-2020',
 'MX', 'Mexican'),

('Isabel',  'Del Carmen', 'García', NULL,
 'isabel.garcia@example.mx',
 'Blvd. Díaz Ordaz 300, Monterrey, NL, 64000',
 '+52-81-5555-3030',
 'MX', 'Mexican'),

('Juan',    'Pablo',   'Santos',  'Gómez',
 'juan.santos@example.mx',
 'Av. Juárez 50, Puebla, PUE, 72000',
 '+52-222-555-4040',
 'MX', 'Mexican');
