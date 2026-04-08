START TRANSACTION;

--
-- Base de données : `harmogestion`
--
CREATE DATABASE IF NOT EXISTS `harmogestion` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE harmogestion;

--
-- Structure de la table `Membre`
--
CREATE TABLE `Membre`(
   `id_membre` INT AUTO_INCREMENT,
   `nom_membre` VARCHAR(30) NOT NULL,
   `prenom_membre` VARCHAR(30) NOT NULL,
   `date_inscription_membre` DATE NOT NULL,
   PRIMARY KEY(`id_membre`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Instrument`
--
CREATE TABLE `Instrument`(
   `id_instrument` INT AUTO_INCREMENT,
   `libelle_instrument` VARCHAR(50) NOT NULL,
   PRIMARY KEY(`id_instrument`),
   UNIQUE(`libelle_instrument`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Cours`
--
CREATE TABLE `Cours`(
   `id_cours` INT AUTO_INCREMENT,
   `date_cours` DATETIME NOT NULL,
   `duree_cours` TINYINT NOT NULL,
   `id_instrument` INT NOT NULL,
   `id_membre_enseignant` INT NOT NULL,
   PRIMARY KEY(`id_cours`),
   FOREIGN KEY(`id_instrument`) REFERENCES Instrument(`id_instrument`),
   FOREIGN KEY(`id_membre_enseignant`) REFERENCES Membre(`id_membre`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Representation`
--
CREATE TABLE `Representation`(
   `id_representation` INT AUTO_INCREMENT,
   `nom_representation` VARCHAR(80) NOT NULL,
   `date_representation` DATETIME NOT NULL,
   `lieu_representation` VARCHAR(100) NOT NULL,
   PRIMARY KEY(`id_representation`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Pratiquer`
--
CREATE TABLE `Pratiquer`(
   `id_membre` INT,
   `id_instrument` INT,
   `apprentissage_en_cours` BOOLEAN NOT NULL,
   PRIMARY KEY(`id_membre`, `id_instrument`),
   FOREIGN KEY(`id_membre`) REFERENCES `Membre`(`id_membre`),
   FOREIGN KEY(`id_instrument`) REFERENCES `Instrument`(`id_instrument`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Participer_Cours`
--
CREATE TABLE `Participer_Cours`(
   `id_membre_apprenant` INT,
   `id_cours` INT,
   PRIMARY KEY(`id_membre_apprenant`, `id_cours`),
   FOREIGN KEY(`id_membre_apprenant`) REFERENCES `Membre`(`id_membre`),
   FOREIGN KEY(`id_cours`) REFERENCES `Cours`(`id_cours`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Participer_Representation`
--
CREATE TABLE `Participer_Representation`(
   `id_membre` INT,
   `id_representation` INT,
   PRIMARY KEY(`id_membre`, `id_representation`),
   FOREIGN KEY(`id_membre`) REFERENCES `Membre`(`id_membre`),
   FOREIGN KEY(`id_representation`) REFERENCES `Representation`(`id_representation`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Structure de la table `Instruments_Representation`
--
CREATE TABLE `Instruments_Representation`(
   `id_instrument` INT,
   `id_representation` INT,
   PRIMARY KEY(`id_instrument`, `id_representation`),
   FOREIGN KEY(`id_instrument`) REFERENCES `Instrument`(`id_instrument`),
   FOREIGN KEY(`id_representation`) REFERENCES `Representation`(`id_representation`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

COMMIT;
