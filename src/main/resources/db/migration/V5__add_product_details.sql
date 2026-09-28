ALTER TABLE products
ADD COLUMN description VARCHAR(500),
ADD COLUMN expiration_date DATE,
ADD COLUMN condition_type VARCHAR(50),
ADD COLUMN photo_urls JSON;
