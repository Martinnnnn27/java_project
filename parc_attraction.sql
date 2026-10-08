-- 1. Création et sélection de la base de données
CREATE DATABASE IF NOT EXISTS parc_attraction;
USE parc_attraction;

-- 2. Table 1 : Authentification des utilisateurs
CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(250) NOT NULL
);

-- 3. Table 2 : Enregistrement des entrées du parc
CREATE TABLE IF NOT EXISTS entree_parc (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom_visiteur VARCHAR(50) NOT NULL,
    prenom_visiteur VARCHAR(50) NOT NULL,
    type_pass VARCHAR(30) NOT NULL,        -- Ex: 'Journée', 'Pass VIP', 'Enfant'
    zone_attraction VARCHAR(50) NOT NULL,  -- Ex: 'Grande Roue', 'Grand Huit', 'Parc Aquatique'
    prix DOUBLE NOT NULL
);

-- 4. Insertion de données de test
INSERT INTO utilisateur (login, password) VALUES 
('admin', 'admin123'),
('alexis', 'parc2026');

INSERT INTO entree_parc (nom_visiteur, prenom_visiteur, type_pass, zone_attraction, prix) VALUES 
('Dupont', 'Jean', 'Pass Journée', 'Grande Roue', 25.50),
('Martin', 'Sophie', 'Pass VIP', 'Grand Huit', 45.00);