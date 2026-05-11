--CREATE DATABASE garageims;

-- Connect to garageims before running the rest.

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'STAFF')),
    full_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS suppliers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(30),
    address TEXT
);

CREATE TABLE IF NOT EXISTS inventory (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    supplier_id INTEGER REFERENCES suppliers(id) ON DELETE SET NULL,
    date_added DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS services (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    vehicle_no VARCHAR(30) NOT NULL,
    service_type VARCHAR(100) NOT NULL,
    cost NUMERIC(10,2) NOT NULL CHECK (cost >= 0),
    service_date DATE NOT NULL DEFAULT CURRENT_DATE,
    notes TEXT
);

CREATE TABLE IF NOT EXISTS bills (
    id SERIAL PRIMARY KEY,
    service_id INTEGER NOT NULL REFERENCES services(id) ON DELETE CASCADE,
    total_amount NUMERIC(10,2) NOT NULL CHECK (total_amount >= 0),
    bill_date DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS tasks (
    id SERIAL PRIMARY KEY,
    assigned_to INTEGER REFERENCES users(id) ON DELETE SET NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    task_date DATE NOT NULL DEFAULT CURRENT_DATE
);

INSERT INTO users (username, password, role, full_name, is_active)
VALUES
('admin', 'admin123', 'ADMIN', 'System Administrator', TRUE),
('staff1', 'staff123', 'STAFF', 'Ravi Mechanic', TRUE),
('staff2', 'staff123', 'STAFF', 'Sanjay Technician', TRUE)
ON CONFLICT (username) DO NOTHING;

INSERT INTO suppliers (name, contact, address)
VALUES
('Speed Auto Parts', '9876543210', 'Pune, Maharashtra'),
('Prime Lubricants', '9988776655', 'Mumbai, Maharashtra')
ON CONFLICT DO NOTHING;

INSERT INTO inventory (name, category, quantity, price, supplier_id, date_added)
VALUES
('Engine Oil 1L', 'Oil', 12, 450.00, 2, CURRENT_DATE),
('Brake Pad Set', 'Engine', 4, 1200.00, 1, CURRENT_DATE),
('Spark Plug', 'Electrical', 20, 180.00, 1, CURRENT_DATE),
('Tubeless Tyre', 'Tyre', 3, 2800.00, 1, CURRENT_DATE);

INSERT INTO services (customer_name, vehicle_no, service_type, cost, service_date, notes)
VALUES
('Amit Patil', 'MH12AB1234', 'Full Service', 2500.00, CURRENT_DATE, 'Oil change and brake inspection'),
('Neha Joshi', 'MH14XY5678', 'Wheel Alignment', 800.00, CURRENT_DATE, 'Alignment completed');

INSERT INTO bills (service_id, total_amount, bill_date)
VALUES
(1, 2500.00, CURRENT_DATE),
(2, 800.00, CURRENT_DATE);

INSERT INTO tasks (assigned_to, title, description, status, task_date)
VALUES
(2, 'Check low stock parts', 'Review tyre and brake pad inventory.', 'Pending', CURRENT_DATE),
(3, 'Prepare service bay', 'Get tools ready for afternoon services.', 'In Progress', CURRENT_DATE);
--SELECT * FROM inventory;
--SELECT * FROM users;