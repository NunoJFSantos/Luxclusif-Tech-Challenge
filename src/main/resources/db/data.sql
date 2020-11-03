DELETE FROM customer;
INSERT INTO customer(ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE, ADDRESS, CITY, COUNTRY) VALUES
    (1, 'Ze Carlos', 'Almeida', 'zecarlos@gmail.com', '912345678', 'Rua do Carmo 127 3 Dir', 'Porto', 'Portugal'),
    (2, 'Luis Miguel', 'Santos', 'luismiguel@hotmail.com', '922222222', 'West St 12', 'London', 'England'),
    (3, 'Fernando', 'Silva', 'fernando@gmail.com', '987654321', 'Rua de Cima 1300', 'Braga', 'Portugal');

DELETE FROM product;
INSERT INTO product(ID, BRAND, YEAR_OF_PURCHASE, PRODUCT_CONDITION, PRICE, CUSTOMER_ID) VALUES
    (1, 'Versace', '2017', 'Good', 200.0, 1),
    (2, 'Prada', '2019', 'Excellent', 150.0, 2),
    (3, 'Louis Vuitton', '2016', 'Good', 500.0, 2),
    (4, 'Chanel', '2015', 'Damaged', 300.0, 3);

DELETE FROM image_file;
INSERT INTO image_file(ID, FILE_NAME, PRODUCT_ID) VALUES
    (1, 'customer1-product1-image0.jpeg', 1);
