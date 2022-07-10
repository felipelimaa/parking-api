CREATE TABLE IF NOT EXISTS Organizations (
    Organization_ID VARCHAR(255) NOT NULL,
    Name VARCHAR(255) NOT NULL,
    Cost DECIMAL(19,2) NOT NULL,
    MaximumCapacity INTEGER NOT NULL,

    PRIMARY KEY (Organization_ID)
)
ENGINE = InnoDB;