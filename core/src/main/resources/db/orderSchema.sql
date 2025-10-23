drop table if exists orders;
drop table if exists orderItems;

create table orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    secureId VARCHAR(35) UNIQUE NOT NULL,
    firstName VARCHAR(50) NOT NULL ,
    lastName VARCHAR(50),
    deliveryAddress VARCHAR(50),
    contactPhoneNo VARCHAR(50) NOT NULL,
    additionalInfo VARCHAR(1000),
    status VARCHAR(20),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subtotal FLOAT,
    deliveryPrice FLOAT,
    totalPrice FLOAT
);

create table orderItems(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orderId BIGINT NOT NULL,
    phoneId BIGINT NOT NULL,
    quantity SMALLINT NOT NULL,
    CONSTRAINT FK_orderItems_orderId FOREIGN KEY (orderId) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_orderItems_phoneId FOREIGN KEY (phoneId) REFERENCES phones (id) ON UPDATE CASCADE
);