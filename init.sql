CREATE TABLE fashion_sales (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255),
    category VARCHAR(50),
    sales_amount DECIMAL(10, 2),
    sales_date DATE,
    store_location VARCHAR(100),
    customer_age_group VARCHAR(50),
    campaign_name VARCHAR(100)
);

INSERT INTO fashion_sales (product_name, category, sales_amount, sales_date, store_location, customer_age_group, campaign_name)
VALUES
    ('Kitenge Blouse', 'Traditional Wear', 2850.00, '2024-03-01', 'Nairobi', '18-24', 'Madaraka Sale'),
    ('Leather Sandals', 'Footwear', 4250.00, '2024-03-01', 'Mombasa', '25-34', 'Madaraka Sale'),
    ('Ankara Maxi Dress', 'Dresses', 3950.00, '2024-03-02', 'Kisumu', '18-24', 'Easter Collection'),
    ('Slim Fit Chinos', 'Casual Wear', 2650.00, '2024-03-03', 'Nairobi', '25-34', 'March Madness'),
    ('Maasai Beaded Loafers', 'Footwear', 5200.00, '2024-03-03', 'Eldoret', '35-44', 'Madaraka Sale'),
    ('Cotton Polo Shirt', 'Tops', 1850.00, '2024-03-04', 'Nakuru', '18-24', 'Easter Collection'),
    ('Denim Jacket', 'Outerwear', 4850.00, '2024-03-05', 'Mombasa', '25-34', 'March Madness');

