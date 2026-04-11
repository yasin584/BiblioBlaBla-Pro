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


USE biblioblabla_pro;


INSERT INTO auteurs (naam) VALUES 
('J.R.R. Tolkien'), 
('George Orwell'), 
('J.K. Rowling'), 
('Agatha Christie'), 
('Stephen King'),
('Lucinda Riley');


INSERT INTO boeken (titel, genre, auteur_id, gemiddelde_beoordeling) VALUES 
('The Hobbit', 'Fantasy', 1, 4.50),
('The Lord of the Rings', 'Fantasy', 1, 4.80),
('1984', 'Dystopian', 2, 4.20),
('Animal Farm', 'Satire', 2, 4.00),
('Harry Potter and the Sorcerer\'s Stone', 'Fantasy', 3, 4.70),
('Harry Potter and the Chamber of Secrets', 'Fantasy', 3, 4.60),
('Murder on the Orient Express', 'Mystery', 4, 4.30),
('Death on the Nile', 'Mystery', 4, 4.10),
('The Shining', 'Horror', 5, 4.40),
('The Seven Sisters', 'Romance', 6, 4.15);

-- 3. Gebruikers toevoegen (wachtwoorden zijn fictieve hashes)
INSERT INTO gebruikers (naam, email, wachtwoord_hash) VALUES 
('Sara', 'sara@gmail.com', '$2a$12$oLtTf97xWJ1C6.PyvzadzOZ1C6DbT.rDVlSx6vOur2HpKfESXyLLi'), -- hash123
('Sisa', 'sisa@gmail.com', '$2a$12$oLtTf97xWJ1C6.PyvzadzOZ1C6DbT.rDVlSx6vOur2HpKfESXyLLi'), -- hash123
('Pieter Post', 'pieter@example.com', '123'),
('Lotte Mulder', 'lotte@example.com', '123'),
('Kees de Boer', 'kees@example.com', '123');

INSERT INTO leningen (gebruiker_id, boek_id, uitleendatum, inleverdatum, beoordeling, is_ingeleverd) VALUES 
(1, 1, '2024-01-05', '2024-01-20', 5, TRUE),
(1, 3, '2024-02-01', '2024-02-15', 4, TRUE),
(2, 5, '2024-01-10', '2024-01-25', 5, TRUE),
(2, 2, '2024-03-01', NULL, NULL, FALSE),
(3, 7, '2024-02-10', '2024-02-28', 4, TRUE),
(3, 9, '2024-03-05', NULL, NULL, FALSE),
(4, 10, '2024-01-15', '2024-02-01', 4, TRUE),
(4, 4, '2024-02-20', '2024-03-05', 3, TRUE),
(5, 1, '2024-02-15', '2024-03-01', 5, TRUE),
(5, 6, '2024-03-10', NULL, NULL, FALSE),
(1, 8, '2024-03-12', NULL, NULL, FALSE),
(2, 4, '2023-12-01', '2023-12-15', 4, TRUE),
(3, 5, '2024-03-15', NULL, NULL, FALSE),
(4, 2, '2024-01-20', '2024-02-10', 5, TRUE),
(5, 3, '2024-02-05', '2024-02-20', 4, TRUE),
(1, 10, '2024-03-20', NULL, NULL, FALSE);



