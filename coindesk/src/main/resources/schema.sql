CREATE TABLE bpi (
    id bigint auto_increment,
    currency_code int not null,
    symbol varchar(10) not null,
    rate varchar(45) not null,
    rate_float NUMERIC(12,4) not null,
    description int NOT NULL,
    created time,
    updated time,
    PRIMARY KEY (id));

CREATE TABLE language (
    id VARCHAR(12) NOT NULL,
    language VARCHAR(45) NULL,
    PRIMARY KEY (id));

CREATE TABLE currency (
    id int NOT NULL,
    currency_code varchar(3) NOT NULL,
    PRIMARY KEY (id));

CREATE TABLE text_column (
    id INT NOT NULL,
    column_name VARCHAR(45) NOT NULL,
    PRIMARY KEY (id));

CREATE TABLE translation (
    language_id int NOT NULL,
    text_column_id int NOT NULL,
    translating_table_uid int NOT NULL,
    translation varchar(45) NOT NULL,
    PRIMARY KEY (language_id,text_column_id,translating_table_uid));

