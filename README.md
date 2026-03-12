# 🎪 Stall Management Service (Bookfair API Network)

**Institution:** Faculty of Engineering, University of Ruhuna
**Module:** EC 8208 - Software Architecture (8th Semester Project)
**Architecture:** Microservices (Spring Boot)
**Internal Port:** `8082`
**API Gateway Port:** `8086`

---

# 📖 Overview

The **Stall Management Service** is a core microservice responsible for handling the physical lifecycle and allocation of exhibition stalls for the Bookfair platform. It manages stall creation, real-time availability tracking, layout categorization by section, and size allocations.

To maintain strict security and data integrity, the service enforces **Role-Based Access Control (RBAC)** via **JWT validation at the API Gateway**.

---

# 👑 Administrator (`ADMIN`) Capabilities

* **Layout Management:** Create new physical stalls, assign them to specific sections (e.g., Main Entrance, VIP Area), and define their sizes.
* **Status Control:** Manually override or update stall statuses (e.g., changing a stall from `AVAILABLE` to `RESERVED` or `BOOKED`).
* **Full CRUD Operations:** Complete authority to update stall details or delete stalls entirely from the floor plan.
* **Global Visibility:** View the complete catalog and details of all stalls.

---

# 👤 Standard User (`USER`) Capabilities

* **Browsing:** View the complete list of all stalls in the bookfair.
* **Availability Checking:** Check the real-time availability status, size, and location of specific stalls to inform booking decisions.

> **Note:** In this microservices architecture, this service sits behind the API Gateway.
> **All external requests must be routed through the Gateway on port `8086`.**
> Direct access to port `8082` should be blocked in a production environment.

---

# 🔐 Security & Authentication (Auth Service)

All Stall endpoints are secured via **JWT validation at the API Gateway level**.

Before accessing the Stall API, you must register a user and obtain a valid token from the **Auth Service**.

*(Auth requests are routed via the API Gateway on port `8086`.)*

---

# Step A: Register a New User

**Method:** `POST`

**URL**

```
http://localhost:8086/api/auth/registeruser
```

**Request Body**

```json
{
  "email": "admin@bookfair.com",
  "password": "password123",
  "role": "ADMIN",
  "companyName": "Bookfair Organizers",
  "contactNumber": "0771234567",
  "owner": "John Doe"
}
```

---

# Step B: Login & Get JWT Token

**Method:** `POST`

**URL**

```
http://localhost:8086/api/auth/login
```

**Request Body**

```json
{
  "email": "admin@bookfair.com",
  "password": "password123"
}
```

**Response**

Copy the token string from the JSON response.

---

# Step C: Authenticate Stall Requests

For all Stall API requests below, include the token in the HTTP headers.

**Header**

```
Authorization: Bearer <YOUR_JWT_TOKEN>
```

⚠️ Ensure there is a space after `Bearer`.

---

# 🚀 Stall API Endpoints & Testing Guide

All URLs below are formatted for testing through the **API Gateway (Port 8086).**

---

# 1️⃣ Create a New Stall

Creates a new physical stall within the bookfair layout.

**Method**

```
POST
```

**URL**

```
http://localhost:8086/api/stalls
```

**Required Role**

```
ADMIN
```

**Request Body**

```json
{
  "stallCode": "A-01",
  "size": "SMALL",
  "status": "AVAILABLE",
  "section": "Main Entrance"
}
```

**Expected Response**

```
201 Created
```

---

# 2️⃣ Get All Stalls

Retrieves a list of all stalls in the system.

**Method**

```
GET
```

**URL**

```
http://localhost:8086/api/stalls
```

**Required Role**

```
ADMIN / USER
```

**Request Body**

```
None
```

**Expected Response**

```
200 OK
```

**Example Response**

```json
[
  {
    "id": 1,
    "stallCode": "A-01",
    "size": "SMALL",
    "status": "AVAILABLE",
    "section": "Main Entrance"
  }
]
```

---

# 3️⃣ Get Stall by ID

Fetch details of a specific stall.

**Method**

```
GET
```

**URL**

```
http://localhost:8086/api/stalls/1
```

Replace `1` with the actual database ID.

**Required Role**

```
ADMIN / USER
```

**Expected Response**

```
200 OK
```

**Example Response**

```json
{
  "id": 1,
  "stallCode": "A-01",
  "size": "SMALL",
  "status": "AVAILABLE",
  "section": "Main Entrance"
}
```

---

# 4️⃣ Update an Existing Stall

Updates the details of an existing stall.

**Method**

```
PUT
```

**URL**

```
http://localhost:8086/api/stalls/1
```

Replace `1` with the actual database ID.

**Required Role**

```
ADMIN
```

**Request Body**

```json
{
  "stallCode": "A-01-MODIFIED",
  "size": "LARGE",
  "status": "RESERVED",
  "section": "VIP Area"
}
```

**Expected Response**

```
200 OK
```

---

# 5️⃣ Update Stall Status Only (Partial Update)

Updates only the stall availability status.

**Method**

```
PATCH
```

**URL**

```
http://localhost:8086/api/stalls/1/status
```

Replace `1` with the actual database ID.

**Required Role**

```
ADMIN
```

**Request Body**

```json
{
  "status": "BOOKED"
}
```

**Expected Response**

```
200 OK
```

---

# 6️⃣ Delete a Stall

Removes a stall completely from the layout.

**Method**

```
DELETE
```

**URL**

```
http://localhost:8086/api/stalls/1
```

Replace `1` with the actual database ID.

**Required Role**

```
ADMIN
```

**Request Body**

```
None
```

**Expected Response**

```
200 OK
```

or

```
204 No Content
```
