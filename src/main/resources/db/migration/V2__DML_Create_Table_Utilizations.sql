CREATE TABLE IF NOT EXISTS Utilizations (
    Utilization_ID BIGINT NOT NULL AUTO_INCREMENT,
    Organization_ID BIGINT NOT NULL,
    Plate VARCHAR(255) NOT NULL,
    Brand VARCHAR(255) NOT NULL,
    Model VARCHAR(255) NOT NULL,
    InitialParkingDate DATETIME NOT NULL,
    FinishParkingDate DATETIME,
    Cost DECIMAL(19,2),
    UtilizationStatus VARCHAR(255) NOT NULL,
    CreatedAt DATETIME NOT NULL,
    UpdateAt DATETIME,

    PRIMARY KEY (Utilization_ID),
    CONSTRAINT `fk_Organization_Utilization`
        FOREIGN KEY (`Organization_ID`)
        REFERENCES `Organizations` (`Organization_ID`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
)
ENGINE = InnoDB;