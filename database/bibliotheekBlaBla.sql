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
    FOREIGN KEY (auteur_id) REFERENCES auteurs(id) ON DELETE CASCADE
);

CREATE TABLE gebruikers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naam VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    wachtwoord_hash VARCHAR(255) NOT NULL,
);

CREATE TABLE leningen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    gebruiker_id INT NOT NULL,
    boek_id INT NOT NULL,
    uitleendatum DATE NOT NULL,
    inleverdatum DATE,
    beoordeling INT CHECK (beoordeling BETWEEN 1 AND 5),
    status ENUM('geleend', 'ingeleverd') DEFAULT 'geleend',
    FOREIGN KEY (gebruiker_id) REFERENCES gebruikers(id),
    FOREIGN KEY (boek_id) REFERENCES boeken(id)
);