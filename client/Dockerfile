# Build react app
FROM node:20-alpine AS build

# Set working directory
WORKDIR /usr/app

# Copy dependencies over to the container
COPY web/package.json web/package-lock.json ./

# Install dependencies
RUN npm install

# Add environment variables from arguments
ARG REACT_APP_GOOGLE_CLIENT_ID
ARG REACT_APP_API_HOST
ENV REACT_APP_GOOGLE_CLIENT_ID $REACT_APP_GOOGLE_CLIENT_ID
ENV REACT_APP_API_HOST $REACT_APP_API_HOST

# Copy app files
COPY web/ .

# Build react app
RUN npm run build

# nginx dist
FROM nginx:1.25.1 AS prod

# Generate temp self-signed certificate to be later overwritten by certbot
RUN mkdir -p /etc/nginx/ssl/live/palettehub.net
RUN openssl req -x509 -nodes -days 3650 -sha256 -newkey rsa:4096 \
    -keyout /etc/nginx/ssl/live/palettehub.net/privkey.pem \
    -out /etc/nginx/ssl/live/palettehub.net/fullchain.pem \
    -subj "/CN=localhost"

# Copy over nginx conf
ARG NGINX_CONF
COPY $NGINX_CONF /etc/nginx/conf.d/default.conf

# Copy over react build
COPY --from=build /usr/app/build /usr/app/

# Copy over dh params
COPY dhparam-2048.pem /etc/nginx/dhparam/dhparam-2048.pem

# Expose port
EXPOSE 80
EXPOSE 3000

# start nginx server
CMD ["nginx", "-g", "daemon off;"]