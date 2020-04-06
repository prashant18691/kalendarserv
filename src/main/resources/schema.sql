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
    start_date_time Timestamp NOT NULL,
    end_date_time Timestamp NOT NULL,
    PRIMARY KEY (slot_id),
    user_id RAW(50) ,
    is_booked CHAR(1) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CONSTRAINT UC_Slot UNIQUE (user_id,start_date_time,end_date_time)
);


CREATE TABLE Slots_Booked (
    book_id RAW(50) NOT NULL,
    slot_id RAW(50) ,
    user_id RAW(50) ,
    PRIMARY KEY (book_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (slot_id) REFERENCES Slot(slot_id),
    CONSTRAINT UK_Slot UNIQUE (user_id,slot_id)
);
