-- ============================================
-- USERS
-- ============================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Subscription type table
CREATE TABLE subscription_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL  -- ex.: 'OCASIONAL', 'ANUAL', 'MENSAL'
);

-- Client table (extends user)
CREATE TABLE client (
    id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    subscription_type_id INT NOT NULL REFERENCES subscription_type(id)
);

-- Admin table (extends user)
CREATE TABLE admin (
    id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- STATIONS
-- ============================================
CREATE TABLE station (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    max_docks INT NOT NULL CHECK (max_docks > 0)
);

-- ============================================
-- BIKES
-- ============================================
CREATE TABLE bike_state (
    id SERIAL PRIMARY KEY,
    description VARCHAR(50) NOT NULL UNIQUE  -- e.g., 'AVAILABLE', 'IN_USE', 'MAINTENANCE', 'RESERVED'
);

CREATE TABLE bike (
    id SERIAL PRIMARY KEY,
    model VARCHAR(255) NOT NULL,
    state INT NOT NULL REFERENCES bike_state(id),
    station_id INT REFERENCES station(id) ON DELETE SET NULL
);

-- ============================================
-- MAINTENANCE OPERATIONS
-- ============================================
CREATE TABLE maintenance (
    id SERIAL PRIMARY KEY,
    bike_id INT NOT NULL REFERENCES bike(id) ON DELETE CASCADE,
    admin_id INT NOT NULL REFERENCES users(id),
    date TIMESTAMP NOT NULL DEFAULT NOW(),
    description TEXT NOT NULL,
    cost DOUBLE PRECISION NOT NULL CHECK (cost >= 0)
);

-- ============================================
-- TRIPS (VIAGENS)
-- ============================================
CREATE TABLE trip (
    id SERIAL PRIMARY KEY,
    bike_id INT NOT NULL REFERENCES bike(id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(id),
    start_station_id INT NOT NULL REFERENCES station(id),
    end_station_id INT REFERENCES station(id),
    start_time TIMESTAMP DEFAULT NOW(),
    end_time TIMESTAMP,
    CHECK (
        (end_time IS NOT NULL AND end_station_id IS NOT NULL)
        OR (end_time IS NULL AND end_station_id IS NULL)
    )
);

-- ============================================
-- WEATHER
-- ============================================
CREATE TABLE IF NOT EXISTS weather_conditions (
    id SERIAL PRIMARY KEY,
    condition VARCHAR(255) NOT NULL
);

-- ============================================
-- SAMPLE DATA
-- ============================================

-- USERS
INSERT INTO users (email, name) VALUES
('alice@example.com', 'Alice'),
('bob@example.com', 'Bob'),
('charlie@example.com', 'Charlie'),
('diana@example.com', 'Diana'),
('admin@admin.admin', 'Admin'),
('user@user.user', 'User');

-- SUBSCRIPTIONS
INSERT INTO subscription_type (name) VALUES
('OCASIONAL'),
('ANUAL'),
('MENSAL');

-- CLIENTS
INSERT INTO client (id, subscription_type_id) VALUES
(1, 1),
(2, 2);

-- ADMINS
INSERT INTO admin (id) VALUES
(3),
(4),
(5),
(6);

-- ============================================
-- STATIONS (novos países e climas)
-- ============================================

-- Países quentes
INSERT INTO station (name, lat, lon, max_docks) VALUES
('Rio de Janeiro Centro', -22.9068, -43.1729, 15),
('Bangkok Central', 13.7563, 100.5018, 12),
('Dubai Marina', 25.0773, 55.1412, 10);

-- Países frios
INSERT INTO station (name, lat, lon, max_docks) VALUES
('Oslo City', 59.9139, 10.7522, 8),
('Reykjavik Downtown', 64.1355, -21.8954, 6),
('Moscow Central', 55.7558, 37.6173, 12);

-- Países amenos
INSERT INTO station (name, lat, lon, max_docks) VALUES
('Lisbon Bairro Alto', 38.7139, -9.1399, 10),
('Barcelona Eixample', 41.3874, 2.1686, 12),
('San Francisco Downtown', 37.7749, -122.4194, 15);

-- ============================================
-- BIKE STATES
-- ============================================
INSERT INTO bike_state (description) VALUES
('AVAILABLE'),
('IN_USE'),
('MAINTENANCE'),
('RESERVED');

-- ============================================
-- NOVAS BIKES
-- ============================================
INSERT INTO bike (model, state, station_id) VALUES
('Modelo E', 1, 1),  -- Rio de Janeiro
('Modelo F', 1, 2),  -- Bangkok
('Modelo G', 1, 3),  -- Dubai
('Modelo H', 1, 4),  -- Oslo
('Modelo I', 1, 5),  -- Reykjavik
('Modelo J', 1, 6),  -- Moscow
('Modelo K', 1, 7),  -- Lisbon
('Modelo L', 1, 8),  -- Barcelona
('Modelo M', 1, 9);  -- San Francisco

-- ============================================
-- NOVAS TRIPS
-- ============================================
INSERT INTO trip (bike_id, user_id, start_station_id, end_station_id, start_time, end_time) VALUES
(1, 1, 1, 2, '2025-12-01 08:00:00', '2025-12-01 08:45:00'),  -- Rio -> Bangkok
(2, 2, 2, 3, '2025-12-02 09:00:00', '2025-12-02 09:30:00'),  -- Bangkok -> Dubai
(3, 1, 3, 1, '2025-12-03 10:00:00', '2025-12-03 10:50:00'),  -- Dubai -> Rio
(4, 2, 4, 5, '2025-12-04 11:00:00', '2025-12-04 11:40:00'),  -- Oslo -> Reykjavik
(5, 1, 5, 6, '2025-12-05 12:00:00', '2025-12-05 12:30:00'),  -- Reykjavik -> Moscow
(6, 2, 6, 4, '2025-12-06 13:00:00', '2025-12-06 13:50:00'),  -- Moscow -> Oslo
(7, 1, 7, 8, '2025-12-07 14:00:00', '2025-12-07 14:30:00'),  -- Lisbon -> Barcelona
(8, 2, 8, 9, '2025-12-08 15:00:00', '2025-12-08 15:45:00'),  -- Barcelona -> San Francisco
(9, 1, 9, 7, '2025-12-09 16:00:00', '2025-12-09 16:50:00');  -- San Francisco -> Lisbon
