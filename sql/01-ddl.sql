USE hotel_database;

CREATE TABLE T_Room (
    c_room_id INT PRIMARY KEY AUTO_INCREMENT,
    c_number INT NOT NULL,
    c_status ENUM('AVAILABLE', 'UNAVAILABLE', 'REPAIR', 'SERVICE') NOT NULL,
    c_price DECIMAL(10, 2) NOT NULL,
    c_capacity INT NOT NULL,
    c_stars INT NOT NULL
);

CREATE TABLE T_Guest (
    c_guest_id INT PRIMARY KEY AUTO_INCREMENT,
    c_guestname VARCHAR(100) NOT NULL,
    c_email VARCHAR(100) NOT NULL UNIQUE,
    c_checkindate DATE NOT NULL,
    c_checkoutdate DATE NOT NULL,
    fk_room_id INT,
    FOREIGN KEY (fk_room_id) REFERENCES T_Room(c_room_id)
);

CREATE TABLE T_Room_History (
    c_history_id INT PRIMARY KEY AUTO_INCREMENT,
    fk_room_id INT NOT NULL,
    fk_guest_id INT NOT NULL,
    c_checkin_date DATE NOT NULL,
    c_checkout_date DATE NOT NULL,
    c_status ENUM('CHECKED_IN', 'CHECKED_OUT') NOT NULL,
    FOREIGN KEY (fk_room_id) REFERENCES T_Room(c_room_id),
    FOREIGN KEY (fk_guest_id) REFERENCES T_Guest(c_guest_id)
);

CREATE TABLE T_Service (
    c_service_id INT PRIMARY KEY AUTO_INCREMENT,
    c_servicename VARCHAR(100) NOT NULL,
    c_price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE T_Provided_Services (
    c_id INT AUTO_INCREMENT PRIMARY KEY,
    fk_guest_id INT NOT NULL,
    fk_service_id INT NOT NULL,
    c_date DATE NOT NULL,
    FOREIGN KEY (fk_guest_id) REFERENCES T_Guest(c_guest_id),
    FOREIGN KEY (fk_service_id) REFERENCES T_Service(c_service_id)
);

CREATE TABLE T_User (
    c_user_id INT AUTO_INCREMENT PRIMARY KEY,
    c_username VARCHAR(50) NOT NULL UNIQUE,
    c_password VARCHAR(255) NOT NULL,
    c_role VARCHAR(50)
);

CREATE TABLE T_Privilege (
    c_privilege_id INT AUTO_INCREMENT PRIMARY KEY,
    c_privilege_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE T_User_Privilege (
    fk_user_id INT NOT NULL,
    fk_privilege_id INT NOT NULL,
    PRIMARY KEY (fk_user_id, fk_privilege_id),
    FOREIGN KEY (fk_user_id) REFERENCES T_User(c_user_id),
    FOREIGN KEY (fk_privilege_id) REFERENCES T_Privilege(c_privilege_id)
);