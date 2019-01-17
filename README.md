# AVV-periodika
Learning project

## Task:
System Periodicals. 
The administrator maintains a catalog of periodicals. 
Reader can subscribe by selecting the Periodicals from the list. 
The system calculates the amount for payment and registers the Payment.

### Installation and running
To install and run the project on localhost:
 * Clone or download the project from GitHub 
 * Create database **_"periodika"_** on your MySQL server. 
 * Edit file "/src/main/webapp/META-INF/context.xml": change keys "username" and "password" to yours. 
 * Execute sql-script from file "/src/main/resources/sql/db_data_init.sql" (ensure created schema **_"periodika"_** with demo data created)                                                        
 * Build project to package **_"periodika.war"_** with command "mvn clean package -Dmaven.test.skip=true". Deploy created file to the Tomcat. 
 * The site will be available at _http://localhost:8087/periodika/_ 
 * Default administrator login: "admin" with empty password
 
 
## Author
AVV
