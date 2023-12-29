# krd-lets-climb-rest
Welcome to krd-lets-climb-rest!

This REST API serves as the backbone for the Let's Climb full stack web application. Let's Climb is a utlity appication that allows users to manage rock climbs by:

* Securely creating an account and profile.
* Creating and maintaining a list of prospective and already completed rock climbs.
* Creating and maintaining a list of attempts for a given rock climb, including adding pictures and a description of their experience. A great way to remember those epic climbs, and to steer clear of the crappy ones!
* Dynamically filtering the user's list of climbs by several climb properties such as name, grade, style, area name, crag name etc.
* Sorting the user's list of climbs by the various climb properties.

## Database
To store all the data required for the Let's Climb full stack web application, I designed a fairly simple MySQL relational database that consists of the following:
#### 4 Primary Tables
1. Climb
2. User
3. Attempt
4. Attempt Image

#### 4 Static Domain Tables
The data in these tables is solely managed by me as the DBA. 
1. Grade - Yosemite Decimal System climbing grades stored in order of easiest to hardest.
2. Boulder Grade - V-Scale bouldering grades stored order of easiest to hardest.
3. Style - Standard climbing styles such as Trad, Sport, Boulder, Ice, and Mixed.
4. Danger - Standard climbing danger ratings such as PG, PG-13, R, and X.

#### Entity Relationship Diagram
![image](https://github.com/KyleRobison15/krd-lets-climb-rest/assets/81257957/fb1ad351-d0be-4a1a-8387-4c01dfff1d51)

## REST Endpoints
This API features 16 thoughtfully designed REST endpoints each providing a 

1. Auth - Handles req
3. Climbs
4. Attempts
5. Auxillary

## Filtering and Sorting

## Exception Handling

## Sring Security using JSON Web Tokens
![image](https://github.com/KyleRobison15/krd-lets-climb-rest/assets/81257957/15216bf9-1d0d-4b72-b30e-144b4586b512)

## Technologies, Libraries and Patterns Used
* Java
* SQL Relational Database (MySQL)
* RESTful Services
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* Swagger OpenAPI
* Customized Exceptions
* Customized Comparators
* Data Transfer Object Pattern (DTO)
