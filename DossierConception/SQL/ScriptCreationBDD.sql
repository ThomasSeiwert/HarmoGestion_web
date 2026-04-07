CREATE DATABASE harmogestion;

USE harmogestion;

CREATE TABLE Membre(
   idMembre INT AUTO_INCREMENT,
   nomMembre VARCHAR(30) NOT NULL,
   prenomMembre VARCHAR(30) NOT NULL,
   dateInscriptionMembre DATE NOT NULL,
   PRIMARY KEY(idMembre)
)ENGINE=InnoDB;

CREATE TABLE Instrument(
   idInstrument INT AUTO_INCREMENT,
   libelleInstrument VARCHAR(50) NOT NULL,
   PRIMARY KEY(idInstrument),
   UNIQUE(libelleInstrument)
)ENGINE=InnoDB;

CREATE TABLE Cours(
   idCours INT AUTO_INCREMENT,
   dateCours DATETIME NOT NULL,
   dureeCours TINYINT NOT NULL,
   idInstrument INT NOT NULL,
   idMembreEnseignant INT NOT NULL,
   PRIMARY KEY(idCours),
   FOREIGN KEY(idInstrument) REFERENCES Instrument(idInstrument),
   FOREIGN KEY(idMembreEnseignant) REFERENCES Membre(idMembre)
)ENGINE=InnoDB;

CREATE TABLE Representation(
   idRepresentation INT AUTO_INCREMENT,
   nomRepresentation VARCHAR(80) NOT NULL,
   dateRepresentation DATETIME NOT NULL,
   lieuRepresentation VARCHAR(100) NOT NULL,
   PRIMARY KEY(idRepresentation)
)ENGINE=InnoDB;

CREATE TABLE Pratiquer(
   idMembre INT,
   idInstrument INT,
   enCoursApprentissage BOOLEAN NOT NULL,
   PRIMARY KEY(idMembre, idInstrument),
   FOREIGN KEY(idMembre) REFERENCES Membre(idMembre),
   FOREIGN KEY(idInstrument) REFERENCES Instrument(idInstrument)
)ENGINE=InnoDB;

CREATE TABLE ParticiperCours(
   idMembreApprenant INT,
   idCours INT,
   PRIMARY KEY(idMembreApprenant, idCours),
   FOREIGN KEY(idMembreApprenant) REFERENCES Membre(idMembre),
   FOREIGN KEY(idCours) REFERENCES Cours(idCours)
)ENGINE=InnoDB;

CREATE TABLE ParticiperRepresentation(
   idMembre INT,
   idRepresentation INT,
   PRIMARY KEY(idMembre, idRepresentation),
   FOREIGN KEY(idMembre) REFERENCES Membre(idMembre),
   FOREIGN KEY(idRepresentation) REFERENCES Representation(idRepresentation)
)ENGINE=InnoDB;

CREATE TABLE InstrumentsRepresentation(
   idInstrument INT,
   idRepresentation INT,
   PRIMARY KEY(idInstrument, idRepresentation),
   FOREIGN KEY(idInstrument) REFERENCES Instrument(idInstrument),
   FOREIGN KEY(idRepresentation) REFERENCES Representation(idRepresentation)
)ENGINE=InnoDB;
