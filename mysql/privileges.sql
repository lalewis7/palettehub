/* Create new user for rest api to access */
CREATE USER '$MYSQL_API_USER'@'%' IDENTIFIED BY '$MYSQL_API_PASSWORD';

/* Grant execute priv to run sprocs */
GRANT EXECUTE ON *.* TO '$MYSQL_API_USER'@'%';

/* Flush privileges to take effect immediately */
FLUSH PRIVILEGES;