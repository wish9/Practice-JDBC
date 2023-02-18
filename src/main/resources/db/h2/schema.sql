CREATE TABLE IF NOT EXISTS MESSAGE (
    message_id bigint NOT NULL AUTO_INCREMENT,
    message varchar(100) NOT NULL,
    PRIMARY KEY (message_id)
);