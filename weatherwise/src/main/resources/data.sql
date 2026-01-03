-- #############################################
-- SQL Data Examples for Weather Wise Project #1
-- Course: Construção de Sistemas de Software (CSS)
-- Semester 1: 2025/2026
-- Grupo: algo
-- #############################################

-- esta é a ordem de inserção devido às foreign keys
--1. Location
-- 2. WeatherCondition
-- 3. Author
-- 4. DailyForecast (depende de Location e WeatherCondition)
-- 5. AuditLog (depende de Author e Location)

INSERT INTO Location (name) VALUES
('Lisboa'),
('Porto'),
('Braga'),
('Faro'),
('Coimbra'),
('Arruda dos Vinhos');

INSERT INTO WeatherCondition (description) VALUES
('Sun'),
('Rain'),
('Thunderstorm'),
('Cloudy'),
('Snow'),
('Windy');


INSERT INTO Author (name) VALUES
('Albertino Josefino'),
('José da Costa'),
('Mariana Silva');


-- IDs de Localização: 1: Lisboa, 2: Porto, 3: Braga, 4: Faro, 5: Coimbra, 6:Arruda dos Vinhos
-- IDs de Condição: 1: Sol, 2: Chuva, 3: Trovoada, 4: Nublado, 5: Neve

-- Dados para Lisboa

INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) VALUES
(1, '2025-10-27', 1, 22.5),
(1, '2025-10-28', 1, 23.0),
(1, '2025-10-29', 4, 21.0),
(1, '2025-10-30', 2, 19.5),
(1, '2025-10-31', 1, 22.0);

-- Dados para o Porto

INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) VALUES
(2, '2025-10-27', 2, 18.0),
(2, '2025-10-28', 4, 19.0),
(2, '2025-10-29', 2, 17.5),
(2, '2025-10-30', 2, 18.0),
(2, '2025-10-31', 4, 19.5);

--Dados para Braga

INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) VALUES
(3, '2025-10-27', 2, 17.0),
(3, '2025-10-28', 4, 18.0),
(3, '2025-10-29', 1, 20.0),
(3, '2025-10-30', 1, 18.0),
(3, '2025-10-31', 4, 19.5);

--Dados para faro - ultimos 15 dias (para a pergunta 4)
-- neste caso a previsão seria "Sol"
INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) VALUES
(4, '2025-10-17', 1, 25.0),
(4, '2025-10-18', 1, 26.0),
(4, '2025-10-19', 1, 25.5),
(4, '2025-10-20', 4, 24.0),
(4, '2025-10-21', 1, 26.5),
(4, '2025-10-22', 2, 22.0),
(4, '2025-10-23', 1, 24.0),
(4, '2025-10-24', 1, 25.0),
(4, '2025-10-25', 1, 27.0),
(4, '2025-10-26', 4, 26.0),
(4, '2025-10-27', 1, 27.5),
(4, '2025-10-28', 1, 28.0),
(4, '2025-10-29', 4, 25.0),
(4, '2025-10-30', 1, 26.0),
(4, '2025-10-31', 1, 27.0);

-- Dados para Coimbra - ultimos 30 dias (pergunta 5)
-- neste caso a previsáo seria "Chuva"

INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) VALUES
(5, '2025-10-02', 2, 19.0),
(5, '2025-10-03', 2, 18.5),
(5, '2025-10-04', 1, 22.0),
(5, '2025-10-05', 4, 20.0),
(5, '2025-10-06', 2, 17.0),
(5, '2025-10-07', 2, 18.0),
(5, '2025-10-08', 3, 16.5),
(5, '2025-10-09', 2, 18.0),
(5, '2025-10-10', 1, 23.0),
(5, '2025-10-11', 4, 21.0),
(5, '2025-10-12', 2, 19.5),
(5, '2025-10-13', 2, 18.0),
(5, '2025-10-14', 1, 22.5),
(5, '2025-10-15', 2, 19.0),
(5, '2025-10-16', 2, 18.5),
(5, '2025-10-17', 4, 20.0),
(5, '2025-10-18', 2, 17.5),
(5, '2025-10-19', 2, 18.0),
(5, '2025-10-20', 1, 21.0),
(5, '2025-10-21', 2, 19.0),
(5, '2025-10-22', 2, 18.0),
(5, '2025-10-23', 4, 19.5),
(5, '2025-10-24', 2, 17.0),
(5, '2025-10-25', 1, 22.0),
(5, '2025-10-26', 2, 19.0),
(5, '2025-10-27', 2, 18.5),
(5, '2025-10-28', 3, 17.0),
(5, '2025-10-29', 2, 18.0),
(5, '2025-10-30', 4, 19.0),
(5, '2025-10-31', 2, 18.5);

-- Dados para arruda



-- IDs de Autor: 1: Albertino Josefino, 2: José da Costa, 3: Mariana Silva

-- Dados para a pergunta 2 - consultas ao Porto
INSERT INTO AuditLog (author_id, location_id, query_timestamp, query_type) VALUES
(1, 2, '2025-10-30 09:15:00', 'CURRENT'),
(2, 2, '2025-10-30 11:05:00', 'CURRENT'),
(1, 2, '2025-10-31 08:30:00', 'ESTIMATE');

-- Dados para a pergunta 3 - consultas a Braga

INSERT INTO AuditLog (author_id, location_id, query_timestamp, query_type) VALUES
(1, 3, '2025-10-28 10:00:00', 'CURRENT'),
(2, 3, '2025-10-29 14:20:00', 'CURRENT'),
(1, 3, '2025-10-29 15:00:00', 'HISTORICAL'),
(3, 3, '2025-10-30 17:00:00', 'ESTIMATE'),
(1, 3, '2025-10-31 10:10:00', 'CURRENT');

-- pergunta 1 

INSERT INTO AuditLog (author_id, location_id, query_timestamp, query_type) VALUES
(2, 1, '2025-11-1 09:00:00', 'CURRENT');

-- pergunta 4

INSERT INTO AuditLog (author_id, location_id, query_timestamp, query_type) VALUES
(3, 4, '2025-10-31 11:30:00', 'ESTIMATE');

-- pergunta 5
INSERT INTO AuditLog (author_id, location_id, query_timestamp, query_type) VALUES
(2, 5, '2025-10-31 12:00:00', 'HISTORICAL');





INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) VALUES
(5, '2025-11-01', 2, 17.5),
(5, '2025-11-02', 2, 18.0),
(5, '2025-11-03', 2, 16.5),
(5, '2025-11-04', 2, 16.0),
(5, '2025-11-05', 4, 19.0),
(5, '2025-11-06', 2, 18.5),
(5, '2025-11-07', 2, 17.0),
(5, '2025-11-08', 2, 16.5),
(5, '2025-11-09', 2, 15.0),
(5, '2025-11-10', 4, 16.0),
(5, '2025-11-11', 2, 16.5),
(5, '2025-11-12', 2, 19.5),
(5, '2025-11-13', 2, 20.0),
(5, '2025-11-14', 2, 17.0),
(5, '2025-11-15', 2, 16.5),
(5, '2025-11-16', 4, 17.0),
(5, '2025-11-17', 2, 15.5),
(5, '2025-11-18', 2, 15.0),
(5, '2025-11-19', 2, 18.0),
(5, '2025-11-20', 4, 17.0),
(5, '2025-11-21', 2, 15.5),
(5, '2025-11-22', 2, 15.0),
(5, '2025-11-23', 2, 14.5),
(5, '2025-11-24', 4, 15.0),
(5, '2025-11-25', 2, 14.0),
(5, '2025-11-26', 2, 14.5),
(5, '2025-11-27', 2, 16.0),
(5, '2025-11-28', 4, 15.5),
(5, '2025-11-29', 2, 13.5),
(5, '2025-11-30', 2, 14.0);

INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) VALUES
(5, '2025-12-01', 2, 13.0),
(5, '2025-12-02', 2, 12.5),
(5, '2025-12-03', 2, 12.0),
(5, '2025-12-04', 4, 15.0),
(5, '2025-12-05', 2, 14.0),
(5, '2025-12-06', 2, 13.5),
(5, '2025-12-07', 2, 13.0),
(5, '2025-12-08', 2, 11.5),
(5, '2025-12-09', 2, 12.0),
(5, '2025-12-10', 4, 11.5),
(5, '2025-12-11', 2, 14.0),
(5, '2025-12-12', 2, 13.0),
(5, '2025-12-13', 2, 12.5),
(5, '2025-12-14', 4, 13.0),
(5, '2025-12-15', 2, 12.0);
