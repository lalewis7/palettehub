/* Create new user for rest api to access */
CREATE USER 'rest_api'@'%' IDENTIFIED BY '$MYSQL_API_USER_PASSWORD';

/* Grant execute priv to run sprocs */
GRANT EXECUTE ON *.* TO 'rest_api'@'%';

/* Flush privileges to take effect immediately */
FLUSH PRIVILEGES;