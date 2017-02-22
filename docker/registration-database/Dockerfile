FROM mysql:5.6

# Copy the database initialize script:
# Contents of /docker-initdb are run on mysqld startup
ADD  docker-initdb/ /docker-initdb/

# Default values for passwords and database name. Can be overridden on docker run
# ENV MYSQL_ROOT_PASSWORD=my-secret-pw # Not defaulted for security reasons!
ENV MYSQL_DATABASE=springapp
ENV MYSQL_USER=springapp
ENV MYSQL_PASSWORD=springapp