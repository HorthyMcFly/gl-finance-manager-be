CREATE TABLE `USER`
(
    ID             INT AUTO_INCREMENT,
    CONSTRAINT USER_PK PRIMARY KEY (ID),
    USERNAME       VARCHAR(20) NOT NULL UNIQUE,
    PASSWORD       VARCHAR(100)  NOT NULL,
    ADMIN          TINYINT NOT NULL,
    ACTIVE         TINYINT NOT NULL
);