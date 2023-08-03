#!/bin/bash
# https://github.com/wmnnd/nginx-certbot/blob/master/init-letsencrypt.sh

if ! [ -x "$(command -v docker)" ]; then
  echo 'Error: docker compose is not installed.' >&2
  exit 1
fi

domains=(palettehub.net www.palettehub.net api.palettehub.net)
rsa_key_size=4096
data_path="/var/lib/docker/volumes/palette_hub_certbot_conf_volume/_data"
email="palettehub.net@gmail.com" # Adding a valid address is strongly recommended
staging=1 # Set to 1 if you're testing your setup to avoid hitting request limits

if [ -d "$data_path" ]; then
  read -p "Existing data found for $domains. Continue and replace existing certificate? (y/N) " decision
  if [ "$decision" != "Y" ] && [ "$decision" != "y" ]; then
    exit
  fi
fi


if [ ! -e "$data_path/options-ssl-nginx.conf" ] || [ ! -e "$data_path/ssl-dhparams.pem" ]; then
  echo "### Downloading recommended TLS parameters ..."
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf > "$data_path/options-ssl-nginx.conf"
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem > "$data_path/ssl-dhparams.pem"
  echo
fi

echo "### Creating dummy certificate for $domains ..."
path="/etc/letsencrypt/live/$domains"
docker exec -d palette_hub_certbot mkdir -p $path && \
  openssl req -x509 -nodes -newkey rsa:$rsa_key_size -days 1 \
    -keyout $path/privkey.pem \
    -out $path/fullchain.pem \
    -subj '/CN=localhost'
echo


echo "### Starting nginx ..."
docker restart palette_hub_client
echo



echo "### Deleting dummy certificate for $domains ..."
docker exec -d palette_hub_certbot \
  rm -Rf /etc/letsencrypt/live/$domains && \
  rm -Rf /etc/letsencrypt/archive/$domains && \
  rm -Rf /etc/letsencrypt/renewal/$domains.conf
echo


echo "### Requesting Let's Encrypt certificate for $domains ..."
#Join $domains to -d args
domain_args=""
for domain in "${domains[@]}"; do
  domain_args="$domain_args -d $domain"
done

# Select appropriate email arg
case "$email" in
  "") email_arg="--register-unsafely-without-email" ;;
  *) email_arg="--email $email" ;;
esac

# Enable staging mode if needed
if [ $staging != "0" ]; then staging_arg="--staging"; fi

docker exec -d palette_hub_certbot \
  certbot certonly --webroot -w /var/www/palettehub \
    $staging_arg \
    $email_arg \
    $domain_args \
    --rsa-key-size $rsa_key_size \
    --agree-tos \
    --force-renewal
echo

echo "### Reloading nginx ..."
docker exec palette_hub_client nginx -s reload