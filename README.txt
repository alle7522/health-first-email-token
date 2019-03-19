=======================
=  Table of Contents  =
=======================
1. Environment Setup and Testing

2. Tools Used

3. Design and Code Flow



======================================
=  1. Environment Setup and Testing  =
======================================

== Setup ==

Clone the project from the Git repo at:
https://github.com/alle7522/health-first-email-token.git


Install Spring Tools Suite 4 (Eclipse) found at:
https://spring.io/tools


Alternatively, follow the instructions at the bottom of the page to add
Spring Tools 4 support to your desired IDE


Open Spring Tools Suite and create/open a workspace as directed


Verify that STS has found a valid Java 1.8 JDK (Not JRE)
(Window -> Preferences -> Java -> Installed JREs)
If it has not, ensure that a recent Java 1.8 JDK is downloaded and 
installed, add it to the installed JREs list and select it


Import the project folder as an Existing Maven Project (File -> Import)


== Testing ==

Right click the project folder in the Package Explorer or Project 
Explorer (left pane) and select Run As -> Spring Boot App to start the
project with the embedded Tomcat server. The console (bottom pane) should
show the startup progress. Startup should only take a few seconds

After the server is started, the web application can be found on 
localhost:80

To test the endpoint, send a GET or POST request to localhost:80/tokenize
with the parameter "email" set to a properly formatted email address
In a browser, this can be done with 
localhost:80/tokenize?email=test@email.com or similar


To run the unit tests, shutdown the server (if running) using the stop 
button in the toolbar, or by right clicking the server in the servers 
panel. Then right click the project folder as before and select 
Run As -> JUnit Test.

This should start the necessary parts of the server, run a series of tests
and display the output in the console and Junit test panel (left, on top 
of the package Explorer)



===================
=  2. Tools Used  =
===================

This project was created in Spring Tools Suite using Spring Boot with the
Spring Web and Test modules: https://spring.io/

JWT token generation and decoding was done using the Java JSON Web Token
(JJWT) library: https://github.com/jwtk/jjwt

Project file hosting by GitHub



=============================
=  3. Design and Code Flow  =
=============================

This project is used to create a token which can be included in an email 
link to verify a user without them having to log in.

Spring Boot is used to simplify the creation of the web server and 
provides tool for quickly creating endpoints.

Requests sent to the server's /tokenize endpoint are directed to the 
emailtoJWT() method in com.email.token.GenerateToken. The method consumes 
an "email" parameter, applying it to the function parameter by the same 
name.

The handler method first verifies that the parameter recieved could 
actually be an email address, and sends a relevant error response if not.

Then it generates an HS256 to HS512 signature key (depending on the length
of the input string), currently using a default value, but could easily be
replaced with a random generator, once a suitable place to store the 
generated key for later is set up

It uses the email address to create a simple JWT token (with the email as 
the subject) and signs it with the key, which it returns as a string in an
HTTP OK response

