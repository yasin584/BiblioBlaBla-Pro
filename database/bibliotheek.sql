-- Database aanmaken
CREATE DATABASE IF NOT EXISTS bibliotheek;
USE bibliotheek;

-- TABEL: USERS

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naam VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- TABEL: AUTEURS

CREATE TABLE auteurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naam VARCHAR(30) NOT NULL
);


-- TABEL: GENRES

CREATE TABLE genres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naam VARCHAR(30) NOT NULL
);


-- TABEL: BOEKEN

CREATE TABLE boeken (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titel VARCHAR(30) NOT NULL,
    auteur_id INT NOT NULL,
    genre_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (auteur_id) REFERENCES auteurs(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);


-- TABEL: UITLEENRECORDS

CREATE TABLE borrow_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    boek_id INT NOT NULL,
    borrow_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (boek_id) REFERENCES boeken(id) ON DELETE CASCADE
);


-- TABEL: RATINGS

CREATE TABLE ratings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    boek_id INT NOT NULL,
    rating TINYINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (boek_id) REFERENCES boeken(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_book (user_id, boek_id)
);