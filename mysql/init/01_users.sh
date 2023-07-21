echo "** Creating api user..."

API_PASSWORD=$(cat $MYSQL_API_PASSWORD_FILE)

# https://stackoverflow.com/questions/68411534/using-environment-variables-in-docker-compose-mounted-files-for-initializing-mys
mysql -u root -p$MYSQL_ROOT_PASSWORD --execute \
"CREATE USER '$MYSQL_API_USER'@'%' IDENTIFIED BY '$API_PASSWORD';
GRANT EXECUTE ON *.* TO '$MYSQL_API_USER'@'%';
FLUSH PRIVILEGES;
"

echo "** Finished creating api user."