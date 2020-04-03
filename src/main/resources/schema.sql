CREATE TABLE Users (
    user_id RAW(50) NOT NULL,
    last_name varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    email_id varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (user_id)
);

CREATE TABLE Slot (
    slot_id RAW(50) NOT NULL,
    slot_date_time Timestamp NOT NULL,
    PRIMARY KEY (slot_id),
    user_id RAW(50),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CONSTRAINT UC_Slot UNIQUE (user_id,slot_date_time)
);
