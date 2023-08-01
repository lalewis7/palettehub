# **Palette Hub MySQL**

## Init Data

All files in the `init` folder are loaded into the docker container upon initialization. This includes:

- `01_users.sh` - creates the API user with limited privilges.
- `02_dump.sql` - strucuture dump containing schemas and sprocs.

When changes are made to the schemas or sprocs and dumped into `02_dump.sql` they should also be saved to `rest_api/src/test/resources/dump.sql` for use in the REST API unit tests.

> Check out the [MySQL Docker Hub page](https://hub.docker.com/_/mysql/) for more information on using the mysql image.

# Schemas

## Users Schema

| Column Name | Datatype          | Notes             |
| ----------- | --------          | -----             |
| **user_id** | CHAR(32)          | primary key, uuid |
| google_id   | [VARCHAR(255)][1] | google id         |
| name        | VARCHAR(64)       | google name       |
| picture_url | VARCHAR(2048)     | google avatar     |
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