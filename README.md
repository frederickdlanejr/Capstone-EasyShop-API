# Capstone EasyShop API

A full-featured e-commerce backend built with Spring Boot and MySQL. This API powers the EasyShop web application, handling products, categories, shopping carts, and order checkout functionality.

---

## 🚀 Features

- 🛍️ Browse products and categories
- 🛒 Add items to shopping cart
- 🧾 View and clear cart
- 🔐 Admin functionality to manage categories
- 🧪 Tested with Postman
- 🗄️ Persistent storage using MySQL

---

## 🧠 Challenges Faced

![Screenshot 2025-06-27 092952](https://github.com/user-attachments/assets/3415a179-3bd7-4c0d-ab94-4085b4851f0a)


---

### 🐛 Cart Not Updating

Cart `POST` requests were failing because `userId` was missing.

**Fix:** Updated frontend to include the required query parameter.

```js
// Before (broken)
axios.post(`/cart/products/${productId}`);

// After (working)
axios.post(`/cart/products/${productId}?userId=1`);
```

---

### ⚙️ MySQL Integration

Custom schema was used for `shopping_cart`, `orders`, and `order_items`:

```sql
CREATE TABLE shopping_cart (
    user_id INT,
    product_id INT,
    quantity INT
);

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
    price DECIMAL(10,2) NOT NULL
);
```

---

## 📚 Category Endpoints

### 🔓 Public

```http
GET /categories
```
Returns a list of all product categories.

---

### 🔐 Admin

```http
GET    /admin/categories
POST   /admin/categories
PUT    /admin/categories/{id}
DELETE /admin/categories/{id}
```

These endpoints let admins create, update, or remove categories such as **Electronics**, **Books**, etc.

---

## 🛒 Shopping Cart Endpoints

### ➕ Add Product to Cart

```http
POST /cart/products/{productId}?userId=1
```

### 📋 View Cart

```http
GET /cart?userId=1
```

### 🗑️ Clear Cart

```http
DELETE /cart?userId=1
```

### ✅ Checkout

```http
POST /orders/checkout?userId=1
```

Creates an order from the user's cart and clears the cart.

---

All endpoints return JSON responses and integrate easily with frontend apps.

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot**
- **MySQL**
- **Maven**
- **Postman**

---

## ✅ Next Steps

- [ ] Add secure login with Spring Security or JWT
- [ ] Replace query param `userId` with authenticated user detection
- [ ] Allow users to view order history
- [ ] Integrate Swagger/OpenAPI documentation for easier testing and documentation
