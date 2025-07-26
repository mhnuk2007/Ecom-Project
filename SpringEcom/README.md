# SpringEcom - E-commerce Backend API

A Spring Boot-based RESTful API for an e-commerce platform with product management and image upload capabilities.

## 🚀 Features

- **Product Management**: Create, read, update, and delete products
- **Image Upload**: Support for product image uploads with multipart file handling
- **Product Search**: Search products by name, description, brand, or category
- **Database Integration**: PostgreSQL database with JPA/Hibernate
- **RESTful API**: Clean REST endpoints with proper HTTP status codes
- **CORS Support**: Configured for frontend integration
- **Data Validation**: Entity validation with proper error handling

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.5.4
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Additional Libraries**: 
  - Lombok (for boilerplate reduction)
  - Jackson (for JSON processing)

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## ⚙️ Setup & Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd SpringEcom
```

### 2. Database Setup
1. Install and start PostgreSQL
2. Create a database named `springecom`:
```sql
CREATE DATABASE springecom;
```

### 3. Configure Database Connection
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/springecom
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Endpoints

### Product Management

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/products` | Get all products | - |
| GET | `/api/product/{id}` | Get product by ID | - |
| GET | `/api/product/{id}/image` | Get product image | - |
| POST | `/api/product` | Create new product | Multipart form data |
| PUT | `/api/product/{id}` | Update product by ID | Multipart form data |
| DELETE | `/api/product/{id}` | Delete product by ID | - |
| GET | `/api/products/search` | Search products by keyword (name, description, brand, or category) | Query parameter: `keyword` |

### API Examples

#### Get All Products
```http
GET http://localhost:8080/api/products
```

#### Get Product by ID
```http
GET http://localhost:8080/api/product/1
```

#### Create New Product
```http
POST http://localhost:8080/api/product
Content-Type: multipart/form-data

{
  "product": {
    "name": "iPhone 15",
    "description": "Latest iPhone model",
    "brand": "Apple",
    "category": "Electronics",
    "releaseDate": "15-09-2023",
    "productAvailable": true,
    "stockQuantity": 50,
    "price": 999.99
  },
  "imageFile": [binary image data]
}
```

#### Get Product Image
```http
GET http://localhost:8080/api/product/1/image
```

#### Update Product
```http
PUT http://localhost:8080/api/product/1
Content-Type: multipart/form-data

{
  "product": {
    "name": "iPhone 15 Pro",
    "description": "Updated iPhone model",
    "brand": "Apple",
    "category": "Electronics",
    "releaseDate": "15-09-2023",
    "productAvailable": true,
    "stockQuantity": 50,
    "price": 999.99
  },
  "imageFile": [binary image data]
}
```
_Response:_
```json
"Updated successfully"
```

#### Delete Product
```http
DELETE http://localhost:8080/api/product/1
```
_Response:_
```json
"Deleted successfully"
```

#### Search Products
```http
GET http://localhost:8080/api/products/search?keyword=iphone
```
_Response:_
```json
[
  {
    "id": 1,
    "name": "iPhone 15",
    "description": "Latest iPhone model",
    "brand": "Apple",
    "category": "Electronics",
    "releaseDate": "15-09-2023",
    "productAvailable": true,
    "stockQuantity": 50,
    "price": 999.99,
    "imageName": "iphone15.jpg",
    "imageType": "image/jpeg"
  }
]
```

## 🗂️ Project Structure

```
src/
├── main/
│   ├── java/com/learning/springecom/
│   │   ├── controller/
│   │   │   └── ProductController.java    # REST endpoints
│   │   ├── model/
│   │   │   └── Product.java              # Product entity
│   │   ├── repo/
│   │   │   └── ProductRepo.java          # JPA repository
│   │   ├── service/
│   │   │   └── ProductService.java       # Business logic
│   │   └── SpringEcomApplication.java    # Main application class
│   └── resources/
│       └── application.properties        # Configuration
└── test/
    └── java/                            # Test files
```

## 📊 Database Schema

### Product Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER (PK) | Auto-generated product ID |
| name | VARCHAR | Product name |
| description | TEXT | Product description |
| brand | VARCHAR | Product brand |
| category | VARCHAR | Product category |
| release_date | DATE | Product release date |
| product_available | BOOLEAN | Availability status |
| stock_quantity | INTEGER | Available stock |
| price | DECIMAL | Product price |
| image_name | VARCHAR | Original image filename |
| image_type | VARCHAR | Image MIME type |
| image_data | BYTEA | Binary image data |

## 🔧 Configuration

### Application Properties
```properties
# Application name
spring.application.name=SpringEcom

# Database configuration
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/springecom
spring.datasource.username=postgres
spring.datasource.password=0000

# JPA/Hibernate configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 🚧 Upcoming Features

- [ ] Pagination for product listings
- [ ] User authentication and authorization
- [ ] Order management system
- [ ] Shopping cart functionality
- [ ] Payment integration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

For questions or support, please contact:
- LinkedIn:[Mohan Lal] https://www.linkedin.com/in/mohan-lal-b79790126/
- GitHub: [@mhnuk2007](https://github.com/mhnuk2007)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community for the robust database
- Lombok project for reducing boilerplate code

## 📝 Technical Notes

- Product images are stored as `byte[]` in the database and can be retrieved via the image endpoint.
- The search endpoint (`/api/products/search`) logs the search keyword to the server console for debugging.
- Database connection uses `spring.datasource.hikari.auto-commit=false` for better transaction control (see `application.properties`).
