# E-commerce Order Management System

This project is built using Java Spring Boot and is designed to manage orders for an online store.

## 📌 Features
- CRUD operations for Products
- Management of Orders and OrderItems
- JWT-based authentication
- Spring Security integration
- Redis support for fast caching or chat functionality
- In-memory H2 database
- API documentation via Swagger
- Email sending functionality (using Spring Mail)

---

## 🛠️ Technologies
- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Web
- Spring Mail
- Redis
- H2 Database
- JWT (JSON Web Token)
- Lombok
- Swagger / OpenAPI

---

## ⚙️ Configuration (`application.properties`)
```properties
spring.application.name=E-commerceOrderManagementSystem

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=50000
spring.mail.properties.mail.smtp.timeout=50000
spring.mail.properties.mail.smtp.writetimeout=50000

# Server
server.port=8080
server.url=http://localhost:8080
```

---

## 🚀 Getting Started

1. **Clone the project:**

```bash
git clone https://github.com/your-username/e-commerce-order-system.git
cd e-commerce-order-system
```

2. **Open the project in your IDE (IntelliJ, VS Code, etc.)**

3. **Start Redis** (if Redis is being used):

   * Windows: `redis-server.exe`
   * Docker: `docker run -p 6379:6379 redis`

4. **Run the Spring Boot application:**

```bash
./mvnw spring-boot:run
```

5. **Access Swagger API documentation:**

   * [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

6. **Access H2 Console:**

   * [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
   * JDBC URL: `jdbc:h2:mem:testdb`
   * Username: `sa`

---

## 🔐 Authentication

JWT-based authentication is used. After logging in, a token is issued which must be sent in future requests using the `Authorization: Bearer <token>` header.

---

## 💬 Chat / Redis Integration

Redis is used for:

* Real-time messaging (chat)
* Caching
* Tracking online users

Make sure Redis is running locally or through Docker.

---

## 📧 Sending Emails

Emails can be sent using endpoints like `/api/email/send`. Gmail SMTP configuration must be set properly in the `application.properties` file.

---

## 📂 Project Structure

```
src
 └── main
     ├── java/com/example
     │   ├── config
     │   ├── controller
     │   ├── dto
     │   ├── entity
     │   ├── repository
     │   ├── service
     │   ├── security
     │   └── ...
     └── resources
         ├── application.properties
         └── static/templates
```

---

## 👨‍💻 Author

**Moxirbek Erkinov**  
Gmail: [erkinov.0021@gmail.com](mailto:erkinov.0021@gmail.com)

---

## 📜 License

This project is open-source and provided for learning purposes only.



**#Language: Uzbek** 


# E-commerce Order Management System

Bu loyiha Java Spring Boot asosida qurilgan va onlayn do'kon uchun buyurtma boshqaruv tizimini yaratishga mo‘ljallangan.

## 📌 Xususiyatlar
- Mahsulotlar (Product) CRUD amallari
- Buyurtmalar (Order) va buyurtma elementlari (OrderItem) boshqaruvi
- JWT asosidagi autentifikatsiya
- Spring Security integratsiyasi
- Redis asosida tezkor keshlash yoki chat imkoniyati
- H2 in-memory ma'lumotlar bazasi
- Swagger orqali API hujjatlari
- Email yuborish funksiyasi (Spring Mail)

---

## 🛠️ Texnologiyalar
- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Web
- Spring Mail
- Redis
- H2 Database
- JWT (JSON Web Token)
- Lombok
- Swagger / OpenAPI

---

## ⚙️ Sozlamalar (`application.properties`)
```properties
spring.application.name=E-commerceOrderManagementSystem

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=50000
spring.mail.properties.mail.smtp.timeout=50000
spring.mail.properties.mail.smtp.writetimeout=50000

# Server
server.port=8080
server.url=http://localhost:8080
````

---

## 🚀 Ishga tushirish

1. **Loyihani yuklab oling:**

```bash
git clone https://github.com/your-username/e-commerce-order-system.git
cd e-commerce-order-system
```

2. **Loyihani IDE (IntelliJ, VS Code) orqali oching**

3. **Redis-ni ishga tushiring** (Agar Redis ishlatilayotgan bo‘lsa):

   * Windows: `redis-server.exe`
   * Docker: `docker run -p 6379:6379 redis`

4. **Spring Boot ilovani ishga tushiring**

```bash
./mvnw spring-boot:run
```

5. **Swagger API hujjatlari:**

   * [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

6. **H2 konsol:**

   * [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
   * JDBC URL: `jdbc:h2:mem:testdb`
   * Username: `sa`

---

## 🔐 Autentifikatsiya

Loyihada JWT asosidagi autentifikatsiya ishlatilgan. Login bo‘limi orqali token olinadi va keyingi so‘rovlar uchun `Authorization: Bearer <token>` sarlavhasi bilan jo‘natiladi.

---

## 💬 Chat yoki Redis integratsiyasi

Loyihada Redis quyidagilar uchun ishlatilishi mumkin:

* Tezkor xabar yuborish (chat)
* Kesh (Caching)
* Online foydalanuvchilarni kuzatish

Redis ishlashi uchun uni lokalda yoki Docker orqali ishga tushiring.

---

## 📧 Email Yuborish

`/api/email/send` kabi endpoint orqali foydalanuvchiga email yuborish mumkin. `application.properties` faylida Google Mail sozlamalari kiritilgan bo'lishi kerak.

---

## 📂 Fayl tuzilmasi

```
src
 └── main
     ├── java/com/example
     │   ├── config
     │   ├── controller
     │   ├── dto
     │   ├── entity
     │   ├── repository
     │   ├── service
     │   ├── security
     │   └── ...
     └── resources
         ├── application.properties
         └── static/templates
```

---

## 👨‍💻 Muallif

**Moxirbek Erkinov**
Gmail: [erkinov.0021@gmail.com](mailto:erkinov.0021@gmail.com)

---

## 📜 Litsenziya

Ushbu loyiha ochiq manbali va o‘rganish maqsadida foydalanish uchun taqdim etilgan.

```

