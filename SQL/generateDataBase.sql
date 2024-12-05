CREATE DATABASE IF NOT EXISTS Bakery;
USE Bakery;

CREATE TABLE `Plate`
(
    `idPlate`         INTEGER AUTO_INCREMENT,
    `plateName`       TINYTEXT NOT NULL,
    `valoration`      INTEGER,
    `uri_preview`     MEDIUMTEXT,
    `idRequiredPlate` INTEGER,
    PRIMARY KEY (`idPlate`)
);


CREATE TABLE `Instruction`
(
    `idInstruction`        INTEGER AUTO_INCREMENT,
    `difficulty`           INTEGER CHECK (`difficulty` BETWEEN 1 AND 5),
    `duration`             INTEGER,
    `instructionText`      TEXT,
    PRIMARY KEY (`idInstruction`)
);


CREATE TABLE `PlateTableCR`
(
    `idPlate`       INTEGER,
    `position`      INTEGER,
    `idInstruction` INTEGER,
    PRIMARY KEY (`idPlate`, `position`)
);


ALTER TABLE PlateInstructionCR
    ADD FOREIGN KEY (`idInstruction`) REFERENCES `Instruction` (`idInstruction`)
        ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE `Plate`
    ADD FOREIGN KEY (`idRequiredPlate`) REFERENCES `Plate` (`idPlate`)
        ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE PlateInstructionCR
    ADD FOREIGN KEY (`idPlate`) REFERENCES `Plate` (`idPlate`)
        ON UPDATE NO ACTION ON DELETE CASCADE;