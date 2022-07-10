CREATE TABLE IF NOT EXISTS Organizations (
    Organization_ID BIGINT NOT NULL AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Cost DECIMAL(19,2) NOT NULL,
    MaximumCapacity INTEGER NOT NULL,

    PRIMARY KEY (Organization_ID),
    CONSTRAINT `OrganizationNameUniqueConstraint` UNIQUE(Name)
)
ENGINE = InnoDB;