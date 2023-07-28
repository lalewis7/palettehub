# **Palettehub REST API**

Palette Hub's Java Spring Boot REST API.

## Controller-Service-Repository

This project follows the controller-service-repository architecture. Here's a good [medium article](https://tom-collings.medium.com/controller-service-repository-16e29a4684e5) to read more about the design.

1. Controller: Management of the REST interface to the business logic.
2. Service: Business Logic implementations.
3. Repository: Storage of the entity beans in the system.

> This project uses the [Spring Boot recommended code structure](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.structuring-your-code).

## Authentication

For registration and logging in clients use a third party service: Google sign in. Although the latest version of google sign in does not handle session management, so the session is handled using a JWT with the user info in the payload.

## Testing

> This project uses [Testcontainers](https://testcontainers.com/) to create containerized test mysql databases.

To the run the unit test scripts you can use the following command:

```
mvn clean test
```

The REST API has a Postman workspace for testing the different endpoints. Check out the workspace with the link below: (**Note: This workspace is not up to date. Instead use the unit tests.**)

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/10733824-9261f960-1a63-4ca8-821e-55fafb61e8b9?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D10733824-9261f960-1a63-4ca8-821e-55fafb61e8b9%26entityType%3Dcollection%26workspaceId%3D08ab66a1-4aa5-4b47-9949-90ef7a55049c)

## API Endpoints Table

| Method     | URL                                   |
| ------     | ---                                   |
| **POST**   | `/auth`                               |
| **GET**    | `/users/:userId`                      |
| **GET**    | `/users/:userId/likes`                |
| **GET**    | `/palettes`                           |
| **POST**   | `/palettes`                           |
| **GET**    | `/palettes/:paletteId`                |
| **POST**   | `/palettes/:paletteId/like`           |
| **DELETE** | `/palettes/:paletteId/like`           |

---

### **[POST]** Create User / Authenitcate

`/auth`

Create a user if a matching google id does not already exist or login if user already has an account.

#### Authorization

No authorization.

#### Query Params

No query params

#### Body

- **credential** *(required)* - Google JWT with google profile provided through google sign in.

#### Response

Palette Hub JWT token for futher authorization.

---

### **[GET]** Get User Profile

`/users/:userId`

Get Palette Hub user profile by the user ID. Use "self" instead of the user ID to use the authentictaed requesters ID.

#### Authorization

JWT - *required*

#### Query Params

No query params

#### Body

No body

#### Response

User model

Error Codes

- **401** - No JWT passed in header.
- **403** - User requesting is not authorized to access user profile.
- **404** - User does not exist with that ID.

---

### **[GET]** Get User Liked Palettes

`/users/:userId/likes`

Get list of liked palettes by user. Use "self" instead of the user ID to use the authentictaed requesters ID.

#### Authorization

JWT - *required*

#### Query Params

- **page** *(optional)* - Used for pagination.

#### Body

No body

#### Response

Palette list model.

Error Codes

- **401** - No JWT passed in header.
- **403** - User requesting is not authorized to access user profile.
- **404** - User does not exist with that ID.

---

### **[GET]** Get List of Palettes

`/palettes`

Get list of palettes by user. Use "self" instead of the user ID to use the authentictaed requesters ID.

#### Authorization

JWT - *optional*

#### Query Params

- **sort** *(required)* - Either "new" or "popular" to order by timestamp and likes.
- **page** *(optional)* - Used for pagination.

#### Body

No body

#### Response

Palette list model.

Error Codes

- **400** - Sort value not given or invalid (must be new or popular).
- **400** - Page value not valid.

---

### **[POST]** Create a Palette

`/palettes`

Create a new palette.

#### Authorization

JWT - *required*

#### Query Params

No query params

#### Body

- **color_1** *(required)* - 6 digit hex code for first color.
- **color_2** *(required)* - 6 digit hex code for second color.
- **color_3** *(required)* - 6 digit hex code for third color.
- **color_4** *(required)* - 6 digit hex code for fourth color.
- **color_5** *(required)* - 6 digit hex code for fifth color.

#### Response

Palette ID

Error Codes

- **401** - No JWT passed in header.
- **400** - Invalid body.

---

### **[GET]** Get Palette

`/palettes/:paletteId`

Get details of palette.

#### Authorization

JWT - *optional*

#### Query Params

No query params

#### Body

No body

#### Response

Palette Model

Error Codes

- **404** - Palette does not exist.

---

### **[POST]** Like a Palette

`/palettes/:paletteId/likes`

Like a palette.

#### Authorization

JWT - *required*

#### Query Params

No query params

#### Body

No body

#### Response

No response

Error Codes

- **401** - No JWT passed in header.
- **404** - Palette does not exist.
- **400** - Palette already liked.

---

### **[DELETE]** Unlike a Palette

`/palettes/:paletteId/likes`

Unlike a palette.

#### Authorization

JWT - *required*

#### Query Params

No query params

#### Body

No body

#### Response

No response

Error Codes

- **401** - No JWT passed in header.
- **404** - Palette does not exist.
- **400** - Palette not liked.

---

## Models

### Palette Model

```json
{
    "palette_id": "948186ea143d11eebe560242ac120002",
    "user_id": "a1b72978143d11eebe560242ac120002",
    "color_1": "FFFFFF",
    "color_2": "123456",
    "color_3": "DDDDDD",
    "color_4": "GGGGGG",
    "color_5": "333333",
    "liked": true, /* For authenticated users -> if user has liked the post. */
    "likes": 23,
    "posted": 1687796575
}
```

### Palette List Model
```json
{
    "palettes": [ /* array of palette models */ ],
    "count": 23 /* Total number of palettes in list (used for pagination) */
}
```

### User Model
```json
{
    "user_id": "a1b72978143d11eebe560242ac120002",
    "google_id": "3141592653589793238",
    "name": "Elisa Beckett",
    "picture_url": "https://lh3.googleusercontent.com/a-/e2718281828459045235360uler",
    "email": "elisa.g.beckett@gmail.com"
}
```