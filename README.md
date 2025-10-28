# Secure-University-Database-System  
## How to connect Percona (MySQL)  
mysql -u root -p                          # connect to mysql server  
mysql -u root -p -h 127.0.0.1 -P 3306     # connect to mysql server using TCP  
\q                                         # exit  


## Quick Start (Local Percona) 

### A) After downloading Percona, run these commands once
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS securedb; \
CREATE USER IF NOT EXISTS 'comp3335'@'localhost' IDENTIFIED BY 'secure_password'; \
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,ALTER,INDEX ON securedb.* TO 'comp3335'@'localhost'; \
FLUSH PRIVILEGES;"

mysql -u root -p < db/schema.sql

### B) Spring Boot Default Setting  
- URL: `jdbc:mysql://127.0.0.1:3306/securedb?useSSL=false&allowPublicKeyRetrieval=true`
- USER: `comp3335`
- PASS: `secure_password`
