CREATE DATABASE IF NOT EXISTS biblioblabla_pro;
USE biblioblabla_pro;

CREATE TABLE auteurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naam VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE boeken (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titel VARCHAR(255) NOT NULL UNIQUE,
    genre VARCHAR(50),
    auteur_id INT NOT NULL,
    gemiddelde_beoordeling DECIMAL(3,2) DEFAULT 0,
    FOREIGN KEY (auteur_id) REFERENCES auteurs(id) ON DELETE CASCADE
);

CREATE TABLE gebruikers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naam VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    wachtwoord_hash VARCHAR(255) NOT NULL

);

CREATE TABLE leningen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    gebruiker_id INT NOT NULL,
    boek_id INT NOT NULL,
    uitleendatum DATE NOT NULL,
    inleverdatum DATE,
    beoordeling INT CHECK (beoordeling BETWEEN 1 AND 5),
    is_ingeleverd BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (gebruiker_id) REFERENCES gebruikers(id),
    FOREIGN KEY (boek_id) REFERENCES boeken(id)
);


USE biblioblabla_pro;

-- AUTEURS
INSERT INTO auteurs (naam) VALUES
('J.K. Rowling'),
('George R.R. Martin'),
('J.R.R. Tolkien'),
('Agatha Christie'),
('Stephen King');

-- BOEKEN
INSERT INTO boeken (titel, genre, auteur_id, gemiddelde_beoordeling) VALUES
('Harry Potter en de Steen der Wijzen', 'Fantasy', 1, 4.80),
('Harry Potter en de Geheime Kamer', 'Fantasy', 1, 4.60),
('Game of Thrones', 'Fantasy', 2, 4.70),
('Clash of Kings', 'Fantasy', 2, 4.50),
('The Hobbit', 'Fantasy', 3, 4.90),
('In de Ban van de Ring', 'Fantasy', 3, 4.95),
('Murder on the Orient Express', 'Mysterie', 4, 4.40),
('It', 'Horror', 5, 4.30);



-- GEBRUIKERS
INSERT INTO gebruikers (naam, email, wachtwoord_hash) VALUES
('Jan Jansen', 'jan@example.com', 'hash123'),
('Piet Pieters', 'piet@example.com', 'hash456'),
('Klaas Klaassen', 'klaas@example.com', 'hash789'),
('Anna de Vries', 'anna@example.com', 'hashabc'),
('Sara Bakker', 'sara@example.com', 'hashdef');

INSERT INTO gebruikers (naam, email, wachtwoord_hash) VALUES
('sara','sara@gmail.com','$2a$12$wdAjzhNmjyLkobzD6epTrOnXR77e40iQpl5HakudNbPG4lBCL3rm2'); -- wachtwoord hash123



-- LENINGEN
INSERT INTO leningen (gebruiker_id, boek_id, uitleendatum, inleverdatum, beoordeling, is_ingeleverd) VALUES
(1, 1, '2026-01-10', '2026-01-20', 5, TRUE),
(1, 3, '2026-02-01', NULL, NULL, FALSE),
(2, 5, '2026-01-15', '2026-01-25', 4, TRUE),
(3, 2, '2026-03-01', NULL, NULL, FALSE),
(4, 7, '2026-02-10', '2026-02-18', 4, TRUE),
(5, 8, '2026-02-20', '2026-03-01', 5, TRUE),
(2, 6, '2026-03-05', NULL, NULL, FALSE);

