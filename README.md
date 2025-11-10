JavaFinalProject is a full-stack Spring Boot e-commerce application with JWT-based authentication. It allows users to browse products, add items to a shopping cart, and place orders. The project also includes role-based access for admin and regular users.

It is designed to demonstrate key backend concepts including:

RESTful API design

JWT authentication and authorization

Spring Data JPA for database interactions

Relationships between entities (User, Product, Cart, Orders)

CRUD operations for products and carts

Secure password storage with BCrypt

Containerization and easy local setup

Key Features
Authentication & Authorization

Users can register and login using email and password.

JWT tokens are issued for authenticated sessions.

Role-based access:

ROLE_ADMIN can manage products and update order status.

ROLE_USER can browse products, manage cart, and checkout.

Product Management

List all products (GET /api/products).

Add, update, delete products (admin only).

Product details include: name, description, price, stock, category, image URL, rating.

Shopping Cart

Each user has a cart.

Users can add, update, or remove products from the cart.

Cart calculates total price automatically.

Order Management

Users can checkout the cart to create an order.

Orders contain order items, quantity, and total amount.

Admin can update the order status (e.g., Pending, Shipped, Delivered).

Database Schema

UserModel: Stores user information (id, name, email, password, role).

ProductModel: Stores products (id, name, description, price, stock, category, image_url, rating).

CartModel & CartItem: Represents user cart and its items.

Order & OrderItem: Stores orders placed by users and their details.

Architecture & Technology Stack
Backend

Spring Boot: REST API development.

Spring Security: JWT authentication and role-based authorization.

Spring Data JPA: Database ORM for entities.

MySQL/PostgreSQL: Relational database (can be swapped).

Entity Relationships

User ↔ Cart: One-to-One

Cart ↔ CartItem: One-to-Many

User ↔ Order: One-to-Many

Order ↔ OrderItem: One-to-Many

Product ↔ CartItem/OrderItem: Many-to-One

Tools

Maven: Project build and dependency management.

Postman: API testing.

Docker: Containerization (optional).

Workflow

User Registration/Login

User registers → password encrypted with BCrypt → JWT generated on login.

Product Browsing

User requests product list → API returns all available products.

Cart Management

Add/Update/Delete products in cart → Cart calculates total price.

Checkout & Order

User checks out cart → Order and OrderItems created → Cart cleared.

Admin can update order status.

Security

Every request (except auth routes) is checked for valid JWT.

Roles determine access permissions for endpoints.

API Endpoints
Auth

POST /api/auth/register

POST /api/auth/login

Products

GET /api/products

POST /api/products (Admin)

PUT /api/products/{id} (Admin)

DELETE /api/products/{id} (Admin)

Cart

POST /api/cart/add/{productId}?userId=&quantity=

PUT /api/cart/update/{productId}?userId=&quantity=

DELETE /api/cart/remove/{productId}?userId=

GET /api/cart?userId=

Orders

POST /api/orders/checkout?userId=

GET /api/orders?userId=

GET /api/orders/{id}

PUT /api/orders/{id}/status?status= (Admin)
