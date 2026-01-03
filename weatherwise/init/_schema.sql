-- #############################################
-- SQL Schema for Weather Wise Project #1
-- Course: Construção de Sistemas de Software (CSS)
-- Semester 1: 2025/2026
-- Grupo: algo
-- #############################################

DROP TABLE IF EXISTS AuditLog;
DROP TABLE IF EXISTS DailyForecast;
DROP TABLE IF EXISTS Author;
DROP TABLE IF EXISTS WeatherCondition;
DROP TABLE IF EXISTS Location;

-- Create Location Table
CREATE TABLE Location (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- Create WeatherCondition Table
CREATE TABLE WeatherCondition (
    id SERIAL PRIMARY KEY,
    description VARCHAR(50) UNIQUE NOT NULL
);

-- Create Author Table (Para normalizar? Diz no enunciado que não é necessário fazer login, mas para seguir boas práticas, criei 'Author' para normalizar a tabela 'AuditLog')
-- This replaces the VARCHAR column in the audit table.
CREATE TABLE Author (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- Create DailyForecast Table
-- Stores the historical record of daily conditions and temperature
CREATE TABLE DailyForecast (
    id SERIAL PRIMARY KEY,
    location_id INTEGER NOT NULL REFERENCES Location(id) ON DELETE CASCADE,
    record_date DATE NOT NULL,
    condition_id INTEGER NOT NULL REFERENCES WeatherCondition(id),
    temperature NUMERIC(4, 1) NOT NULL, -- Ex: 25.5
    CONSTRAINT unique_location_date UNIQUE (location_id, record_date)
);


-- Create AuditLog Table
-- Records all queries made to the system (Use Case C)
CREATE TABLE AuditLog (
    id SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL REFERENCES Author(id) ON DELETE RESTRICT, 
    location_id INTEGER NOT NULL REFERENCES Location(id) ON DELETE RESTRICT,
    query_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    query_type VARCHAR(50) NOT NULL -- Ex: 'CURRENT', 'HISTORICAL', 'ESTIMATE' (Para os exemplos de qiestap dadps)
);

-- Create an index to optimize location-based queries in the audit log
-- Basicamente ajuda a fazer queries no futuro, serve para 'indexar' para facilitar as buscas por localidade. mais em: (https://www.w3schools.com/SQL/sql_ref_index.asp)
CREATE INDEX idx_audit_location ON AuditLog (location_id);