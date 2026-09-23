# Smart Locker REST API

Spring Boot 3 / Java 17 implementation for the smart parcel locker user stories in `22115053122304_DoThaiBinh_DATN_2405.pdf`.

## Run

```bash
mvn spring-boot:run
```

The default database is an in-memory H2 database. H2 console: `http://localhost:8080/h2-console`, JDBC URL `jdbc:h2:mem:smartlocker`.

## Main endpoints

- `POST /api/auth/register/recipient`, `POST /api/auth/register/delivery-staff`, `POST /api/auth/login`
- `GET/PATCH /api/users/{id}`
- `GET /api/admin/delivery-staff/pending`, `PATCH /api/admin/delivery-staff/{id}/approval`
- CRUD banks: `/api/admin/banks`; CRUD carriers: `/api/admin/carriers`
- `GET/POST /api/lockers`, `PATCH /api/lockers/{id}/connection`
- `POST /api/orders`, `GET /api/orders/recipient/{recipientId}`
- Order actions: `/deposit`, `/pay`, `/receive`, `/request-return`, `/return`, `/cancel`

Delivery-staff registration starts in `PENDING`; only an approved staff account can log in. Passwords are BCrypt-hashed and login returns a JWT containing the user id and role. The development seed account is `admin@smartlocker.local` / `Admin@123`; change it before deployment.