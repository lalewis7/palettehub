# PaletteHub MySQL

## Dumps

Structure dumps are stored in the `/dumps` folder and loaded into the docker container upon initialization. Modify which file is loaded by changing the `PALETTE_HUB_MYSQL_DUMP` variable in the `.env` file in the root folder.

## Users Schema

| Column Name | Datatype          | Notes             |
| ----------- | --------          | -----             |
| **user_id** | CHAR(32)          | primary key, uuid |
| google_id   | [VARCHAR(255)][1] | google id         |
| name        | VARCHAR(64)       | google name       |
| picture_url | VARCHAR(248)      | google avatar     |
| email       | VARCHAR(256)      | google email      |


## Palettes Schema

| Column Name      | Datatype     | Notes             |
| -----------      | --------     | -----             |
| **palette_id**   | CHAR(32)     | primary key, uuid |
| **user_id**      | CHAR(32)     | foreign key       |
| color_1          | CHAR(6)      | hex value         |
| color_2          | CHAR(6)      | hex value         |
| color_3          | CHAR(6)      | hex value         |
| color_4          | CHAR(6)      | hex value         |
| color_5          | CHAR(6)      | hex value         |
| posted_timestamp | INT(11)      | epoch timestamp   |

## Likes Schema

| Column Name    | Datatype     | Notes       |
| -----------    | --------     | -----       |
| **palette_id** | CHAR(32)     | foreign key |
| **user_id**    | CHAR(32)     | foreign key |

[1]: https://developers.google.com/identity/openid-connect/openid-connect#an-id-tokens-payload