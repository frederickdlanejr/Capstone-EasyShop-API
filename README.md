Capstone EasyShop API
A Spring Boot–based REST API for an e-commerce platform that supports products catalog, shopping cart, order checkout, and integrates with a frontend website.

🚀 Features
Products: View all products and details

Shopping Cart: Add items to a user’s cart, view current cart, and clear cart

Checkout: Submit cart as an order, storing orders and order_items in the database, then clear the cart

🛠️ Setup & Installation
Prerequisites
Java 17+

Maven

MySQL database

A local copy of the Capstone-EasyShop-API backend

Database
Run this SQL (or import via migration tool):

sql
Copy
Edit
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date DATETIME NOT NULL,
    total DECIMAL(10,2) NOT NULL
);

CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);
Ensure your existing tables include:

products

shopping_cart (columns: user_id, product_id, quantity)

Configuration
In application.properties:

properties
Copy
Edit
spring.datasource.url=jdbc:mysql://localhost:3306/easyshop
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
Build & Run
bash
Copy
Edit
./mvnw clean package
java -jar target/Capstone-EasyShop-API-0.0.1-SNAPSHOT.jar
Backend will listen on http://localhost:8080.

📋 API Endpoints
Products
bash
Copy
Edit
GET /api/products
GET /api/products/{id}
Shopping Cart
bash
Copy
Edit
POST /cart/products/{productId}?userId={userId}
GET  /cart?userId={userId}
DELETE /cart?userId={userId}
Checkout
bash
Copy
Edit
POST /orders/checkout?userId={userId}
Submits cart as an order, clears cart, and stores order + items in DB.

📁 Project Structure
css
Copy
Edit
src/
├─ models/
│   ├─ Product.java
│   ├─ ShoppingCart.java
│   ├─ ShoppingCartItem.java
│   ├─ Order.java
│   └─ OrderItem.java
├─ data/
│   ├─ ProductDao.java
│   ├─ ShoppingCartDao.java
│   ├─ OrderDao.java
│   └─ mysql/
│       ├─ MySqlProductDao.java
│       ├─ MySqlShoppingCartDao.java
│       └─ MySqlOrderDao.java
└─ controllers/
    ├─ ProductController.java
    ├─ ShoppingCartController.java
    └─ OrderController.java
    
📦 Intergration With Frontend




✉️ Need Help?
Reach me at frederick.d.lane@example.com or open an issue on this repo.
