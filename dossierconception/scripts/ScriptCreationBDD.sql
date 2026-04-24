START TRANSACTION;

--
-- Base de données : `harmogestion`
--
CREATE DATABASE IF NOT EXISTS `harmogestion` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE harmogestion;

--
-- Structure de la table `Membre`
--
CREATE TABLE `membre`(
   `id_membre` INT AUTO_INCREMENT,
   `nom_membre` VARCHAR(30) NOT NULL,
   `prenom_membre` VARCHAR(30) NOT NULL,
   `date_inscription_membre` DATE NOT NULL,
   PRIMARY KEY(`id_membre`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Instrument`
--
CREATE TABLE `instrument`(
   `id_instrument` INT AUTO_INCREMENT,
   `libelle_instrument` VARCHAR(50) NOT NULL,
   PRIMARY KEY(`id_instrument`),
   UNIQUE(`libelle_instrument`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Cours`
--
CREATE TABLE `cours`(
   `id_cours` INT AUTO_INCREMENT,
   `date_cours` DATETIME NOT NULL,
   `duree_cours` TINYINT NOT NULL,
   `id_instrument` INT NOT NULL,
   `id_membre_enseignant` INT NOT NULL,
   PRIMARY KEY(`id_cours`),
   FOREIGN KEY(`id_instrument`) REFERENCES instrument(`id_instrument`),
   FOREIGN KEY(`id_membre_enseignant`) REFERENCES membre(`id_membre`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Representation`
--
CREATE TABLE `representation`(
   `id_representation` INT AUTO_INCREMENT,
   `nom_representation` VARCHAR(80) NOT NULL,
   `date_representation` DATETIME NOT NULL,
   `lieu_representation` VARCHAR(100) NOT NULL,
   PRIMARY KEY(`id_representation`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Instrument_Maitrise`
--
CREATE TABLE instrument_maitrise(
    id_membre INT,
    id_instrument INT,
    PRIMARY KEY(id_membre, id_instrument),
    FOREIGN KEY(id_membre) REFERENCES membre(id_membre),
    FOREIGN KEY(id_instrument) REFERENCES instrument(id_instrument)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Instrument_Appris`
--
CREATE TABLE instrument_appris(
    id_membre INT,
    id_instrument INT,
    PRIMARY KEY(id_membre, id_instrument),
    FOREIGN KEY(id_membre) REFERENCES membre(id_membre),
    FOREIGN KEY(id_instrument) REFERENCES instrument(id_instrument)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Participer_Cours`
--
CREATE TABLE `participer_cours`(
   `id_membre_apprenant` INT,
   `id_cours` INT,
   PRIMARY KEY(`id_membre_apprenant`, `id_cours`),
   FOREIGN KEY(`id_membre_apprenant`) REFERENCES `membre`(`id_membre`),
   FOREIGN KEY(`id_cours`) REFERENCES `cours`(`id_cours`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Participer_Representation`
--
CREATE TABLE `participer_representation`(
   `id_membre` INT,
   `id_representation` INT,
   PRIMARY KEY(`id_membre`, `id_representation`),
   FOREIGN KEY(`id_membre`) REFERENCES `membre`(`id_membre`),
   FOREIGN KEY(`id_representation`) REFERENCES `representation`(`id_representation`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Instruments_Representation`
--
CREATE TABLE `instruments_representation`(
   `id_instrument` INT,
   `id_representation` INT,
   PRIMARY KEY(`id_instrument`, `id_representation`),
   FOREIGN KEY(`id_instrument`) REFERENCES `instrument`(`id_instrument`),
   FOREIGN KEY(`id_representation`) REFERENCES `representation`(`id_representation`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

COMMIT;

USE harmogestion;

--
-- Données de la table `Instrument`
--
INSERT INTO instrument (libelle_instrument)
VALUES ('guitare');

--
-- Données de la table `Membre`
--
INSERT INTO membre (nom_membre, prenom_membre, date_inscription_membre)
VALUES ('Hendrix', 'Jimmi', '2026-04-12'),
       ('Seiwert', 'Thomas', '2026-04-14'),
       ('Didier', 'Cédric', '2026-04-15');

--
-- Données de la table `Cours`
--
INSERT INTO cours (date_cours, duree_cours, id_instrument, id_membre_enseignant)
VALUES (DATE_ADD(NOW(), INTERVAL 1 DAY), 45, 1, 1);

--
-- Données de la table `Participer_Cours`
--
INSERT INTO participer_cours (id_membre_apprenant, id_cours)
VALUES (2,1),
       (3,1);