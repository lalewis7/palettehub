# Palette Hub Client

This directory contains all the client code.

## Web - Main Website

The main website made using `create-react-app`.

## Nginx Web Server

### HTTP Traffic

Redirect to https uri.

### www.palettehub.net

Redirect to palettehub.net.

### palettehub.net

Serve static react app build.

### api.palettehub.net

Reverse proxy to Spring Boot REST API.

## Setting up SSL

Setting up the SSL certificates using the certbot for Let's Encrypt proved to be difficult due to the memory issues with docker. Additionally there were issues with the running the script `scripts/init_letsencrypt.sh` so this tutorial will show the manual way while stop containers to save memory.

### 1. Setup Environment

#### 1.1 Start Production

```sh
sudo bash scripts/start_prod.sh
```

#### 1.2 Stop all containers but certbot

```sh
sudo docker stop palette_hub_mysql && sudo docker stop palette_hub_rest_api && sudo docker stop palette_hub_client
```

### 2. Creating dummy certificate

#### 2.1 Download recommended TLS parameters

```sh
sudo curl -o /var/lib/docker/volumes/palette_hub_certbot_conf_volume/_data/options-ssl-nginx.conf https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf
```

```sh
sudo curl -o /var/lib/docker/volumes/palette_hub_certbot_conf_volume/_data/ssl-dhparams.pem https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem
```

Check that the files are in the volume:

```sh
sudo ls /var/lib/docker/volumes/palette_hub_certbot_conf_volume/_data
```

You should see these two files:

- `options-ssl-nginx.conf`
- `ssl-dhparams.pem`

#### 2.2 Open shell to certbot

```sh
sudo docker exec -it palette_hub_certbot sh
```

#### 2.3 Make cert directory

```sh
mkdir -p /etc/letsencrypt/live/palettehub.net
```

#### 2.4 Create self-signed certificates with openssl

```sh
openssl req -x509 -nodes -newkey rsa:4096 -days 1 -keyout /etc/letsencrypt/live/palettehub.net/privkey.pem -out /etc/letsencrypt/live/palettehub.net/fullchain.pem -subj '/CN=localhost'
```

#### 2.5 Exit shell

```sh
exit
```

#### 2.6 Check files were added

```sh
sudo ls /var/lib/docker/volumes/palette_hub_certbot_conf_volume/_data/live/palettehub.net
```

You should see these files:

- `fullchain.pem`
- `privkey.pem`

### 3. Restart Other services

Now the nginx server should be able to run fine with the self signed cert

```sh
sudo docker restart palette_hub_mysql && sudo docker restart palette_hub_rest_api && sudo docker restart palette_hub_client
```

#### 3.1 Check the container is running

```sh
sudo docker ps
```

Look for `palette_hub_client`

#### 3.2 Close unneeded services

The rest api is required for the nginx server to start but after it's stable you can stop it to save resources.

```sh
sudo docker stop palette_hub_mysql && sudo docker stop palette_hub_rest_api
```

### 4. Delete self-signed cert

#### 4.1 Open shell to certbot

```sh
sudo docker exec -it palette_hub_certbot sh
```

#### 4.2 Delete self-signed cert

```sh
rm /etc/letsencrypt/live/palettehub.net/*
```

### 5. Request Let's Encrypt cert

While still in the credbot shell:

```sh
certbot certonly --webroot -w /var/www/palettehub --email palettehub.net@gmail.com -d palettehub.net -d www.palettehub.net -d api.palettehub.net --rsa-key-size 4096 --agree-tos
```

### 6. Restart environment

#### 6.1 Shutdown compose

```sh
sudo docker compose -f docker-compose.yaml -f docker-compose.prod.yaml down
```

#### 6.2 Start containers again

```sh
sudo bash scripts/start_prod.sh
```