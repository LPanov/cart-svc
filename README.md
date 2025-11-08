# 🛒 Cart Service (cart-svc)

This repository contains the source code for the **Cart Microservice (cart-svc)**, a dedicated service responsible for managing user shopping cart data and associated business logic. It operates as a separate domain service, maintaining its own database for persistence.

## Key Features

The Cart Service provides a transactional domain for all cart-related operations, ensuring **consistency** and **high availability** for the checkout process.

* **Isolated Data:** Uses a separate database, adhering to the microservice principle of decentralized data management.
* **Core CRUD Operations:** Handles the creation, retrieval, updating, and deletion of cart contents.
* **Idempotent Cart Management:** The add-to-cart operation acts as an **upsert** (Update if Exists, Insert otherwise) to manage item quantities efficiently.

## Architecture & Persistence

The Cart Microservice (`cart-svc`) is designed for **data isolation** and **resilience**. It leverages Spring Boot and follows a layered architectural approach to manage complexity.

### Data Model

The service is built around two primary **JPA entities** to manage cart state:

| Entity | Role | Key Relationship |
| :--- | :--- | :--- |
| **`Cart`** | Represents the user's shopping cart. | **One-to-Many** relationship with `CartItem`. |
| **`CartItem`** | Represents a single product/part and quantity within a `Cart`. | **Many-to-One** relationship with `Cart`. |

### Communication

Communication with the Main API Gateway/Service is handled via synchronous REST API calls.

* **Client:** The Main API consumes this service using a Feign Client for declarative and resilient HTTP communication.

* **Protocol:** Standard HTTP methods (GET, POST, DELETE).

## Integrations
**Feign Client Configuration (Main API Example)**
The client consuming this service should define a Feign interface similar to the following (assuming your service is registered as cart-svc):

Java

```
@FeignClient(name = "cart-svc", path = "/api/v1")
public interface CartClient {
    
    @GetMapping("/cart")
    ResponseEntity<List<CartItemResponse>> getCartItemsByUserId(@RequestParam("userId") UUID userId);

    @PostMapping("/cart/item")
    ResponseEntity<Void> addToCart(@RequestBody CartItemRequest cartItemRequest);
    
    // ... other methods
}
```
