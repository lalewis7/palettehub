# <img src="./client/web/src/assets/logo.svg" width="26"/> Palette Hub

Palette Hub is a social media site for sharing palettes of hex code colors. Users can create, browse, and share palettes, as well as engage with them through “likes”. Palette Hub does not store user credentials on the site but instead uses Google’s third-party authentication service “Sign In with Google for Web.” Becoming a user — which unlocks all of the site’s available features — is simple and only requires a Google account to sign up.

The purpose of Palette Hub is to showcase a full stack website including Database Management, Backend REST API, Frontend Website, and Container Orchestration. I diversified the stack to exhibit a wide range of skills. The stack includes a MySQL Database, Spring Boot REST API, Create-react-app website, Nginx web server, and Docker Compose container orchestration.

## The Stack

### Deployment

- Docker images
- Azure VM
- Docker Compose Container Orchestration
- Bash scripts

### REST API

- Java Spring Boot
- JWT Authentication
- Sign In with Google for Web
- Apache Maven
- Testcontainers

### MySQL Database

- Schema design
- Stored procedures

### Nginx Web Server

- Reverse Proxy to REST API
- Serve react app build
- Let's Encrypt (SSL Certificate) with certbot

### React App

- Create react app
- Bootstrap
- Bootstrap icons
- SASS
- Google Analytics

## Scripts

### Development Environment

To start the development containers on your local machine use the following commands:

#### Windows

```cmd
scripts\start_dev.cmd
```

#### Linux

```sh
scripts/start_dev.sh
```

### Production Enviroment

Production it setup on an Azure VM.

Shutdown the running containers:

```sh
sudo docker compose -f docker-compose.yaml -f docker-compose.prod.yaml down
```

Pull the changes to the server:

```sh
git pull
```

Delete any images that changes were made to:

```sh
sudo docker rmi (palette_hub_client,palette_hub_rest_api)
```

Start up server:

```sh
sudo bash scripts/start_prod.sh
```

## Docker 

### Container Orchestration

This project uses [Docker Compose](https://docs.docker.com/compose/) for container orchestration which sets up the containers on a single host machine. In later versions it will migrate to [Kubernetes](https://kubernetes.io/) for production to improving scaling the services and using multiple host machines.

The configuration for the containers can be found in the docker compose and env files:

- `docker-compose.yaml`
- `docker-compose.dev.yaml` *(Only on dev env)*
- `docker-compose.prod.yaml` *(Only on prod env)*
- `.env` *(environment variables)*

The environment uses these containers:

- MySQL server.
- Spring Boot REST API server.
- Nginx web server that serves Create-react-app and reverse proxy traffic to the REST API.
- Certbot for SSL certificate renewal. *(production only)*

### Secrets

In development mode the docker compose is setup to use the secrets found in the `/secrets` folder. In production mode the secrets are retrieved from the `$PALETTE_HUB_SECRETS` folder. Set the environment variable in `/etc/environment` for system-wide access.

These are the files that should be included:

- db_api_pass.txt
- jwt_secret.txt
- db_root_pass.txt

## Client

The client directory contains all the client code. Currently this project only has a website. In later versions an admin website will be introduced as well as a mobile app.

### Nginx

All traffic goes through the Nginx web server. HTTP traffic is redirected to HTTPS traffic. The subdomain `api.palettehub.net` is setup as a reverse proxy to the Spring Boot REST API. For more information checkout `/client`.

### React app

The website was made using [Create-react-app](https://create-react-app.dev/) along with [React Bootstrap](https://react-bootstrap.netlify.app/) using [Bootstrap 5](https://getbootstrap.com/) for UI design. For more information checkout `/client/web`.

## REST API

The REST API uses the [Java Spring Boot Framework](https://spring.io/projects/spring-boot) using [Maven](https://maven.apache.org/) for dependency management. Unit tests are setup with [Testcontainers](https://testcontainers.com/). For more information checkout  `/rest_api`.

## MySQL

This project uses a [MySQL](https://www.mysql.com/) Database. The schemas and Stored Procedures were designed using [MySQL Workbench](https://www.mysql.com/products/workbench/). A user is created for the REST API to access the Database. This user is limited to only calling sprocs for security purposes incase the api is compromised. For more information checkout `/mysql`.