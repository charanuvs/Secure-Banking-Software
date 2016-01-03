Secure Online Banking
=======

## Softwares
 1. Java - **jdk1.8.0_60**
 2. Maven -  **Maven 3.3.3**
 3. Apache Tomcat - **apache-tomcat-7.0.64**

## Steps to Integrate with eclipse
 1. Import the `"SecureBankingApp"` folder as a maven project into your eclipse.
 2. Make sure that your Eclipse points to the jdk version mentioned above to build.
 3. To run right click on the project and "Run on server". Configure the server that matches the version above.
 4. Or you can build the project with the command `"mvn clean install"` and then deploy the war file on the server.

The log file goes into `/log` directory. Make sure that you have read and write permissions.

## About
### Security Features
 1. HTTPS/SSL
 2. One-Time Password (OTP) for two factor authentication
 3. Public Key Infrastructure
 4. Role based access
 5. Hashed and Salted Passwords
 6. Virtual Keyboard

### Attacks taken care of
 1. SQL injection attacks
 2. XSS attacks
 3. CSRF attacks
 4. Session Hijacking
