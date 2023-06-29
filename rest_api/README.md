# **PaletteHub Java Spring Boot REST API**

> This project uses the [Spring Boot recommended code structure](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.structuring-your-code).

## API Endpoints

| Method     | URL                                   | Action                                      |
| ------     | ---                                   | ------                                      |
| **POST**   | `/auth`                               | login user (entry point for google sign in) |
| **GET**    | `/users/{userId}/likes`               | get list of liked palettes by user          |
| **GET**    | `/palettes?sort={new,popular}&page=1` | get list of palettes                        |
| **POST**   | `/palettes`                           | create a new palette                        |
| **GET**    | `/palettes/{paletteId}`               | get details of palette                      |
| **POST**   | `/palettes/{paletteId}/likes`         | like a palette                              |
| **DELETE** | `/palettes/{paletteId}/likes`         | unlike a palette                            |

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