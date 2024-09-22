# Welcome to krd-lets-climb-rest!

View the detailed API Docs here: [http://167.172.208.26:31276/swagger-ui/index.html](http://167.172.208.26:31276/swagger-ui/index.html)

Quick Links:
* [Overview](https://github.com/KyleRobison15/krd-lets-climb-rest/blob/main/README.md#overview)
* [Database](https://github.com/KyleRobison15/krd-lets-climb-rest/blob/main/README.md#database)
* [REST Endpoints](https://github.com/KyleRobison15/krd-lets-climb-rest/blob/main/README.md#rest-endpoints)
* [Filtering and Sorting](https://github.com/KyleRobison15/krd-lets-climb-rest/blob/main/README.md#filtering-and-sorting)
* [Uniform Exception Handling](https://github.com/KyleRobison15/krd-lets-climb-rest/blob/main/README.md#uniform-exception-handling)
* [Spring Security Using JSON Web Tokens](https://github.com/KyleRobison15/krd-lets-climb-rest/blob/main/README.md#sring-security-using-json-web-tokens)
* [Technologies Used](https://github.com/KyleRobison15/krd-lets-climb-rest/blob/main/README.md#technologies-libraries-and-patterns-used)

## Overview
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
This API features 16 thoughtfully designed REST endpoints that facilitate efficient and scaleable communication between a client and the krd-lets-climb-rest Java services. I have separated my controllers and services into 4 main categories, each handling a different resource/functionality.

1. Auth - Handles registration and authentication requests.
3. Climbs - Handles CRUD operations on the Climb resource.
4. Attempts - Handles CRUD operations on the Attempt resource.
5. Auxillary - Handles Read Only requests to retrieve data from any one of the four letsclimbdb domain tables.

For detailed documentation of each of the endpoints in this API, you can visit the API docs here: http://localhost:8080/swagger-ui/index.html

## Filtering and Sorting
The GET /api/v1/climbs endpoint provides several optional query parameters for dynamic sorting and filtering. To implement this, I utilized the JpaSpecificationExecutor interface from Spring Data JPA.  Each request made to this GET endpoint first builds up a query specification containing all the provided sorting and filtering query parameters. The result is a unique SQL query for retrieving a filtered and sorted list of climbs from the database all in one fell swoop!

This filtering and sorting functionality is complete with the ability to use expressions such as ">=" on certain fields, as well as exception handling that will intercept any invalid parameters and return a uniform validation error response if necessary.

In addition, a custom comparator for sorting by Yosemite Decimal System climbing grades is also used to allow users to sort climbs in order of ASC or DESC difficulty. This was necessary since the natural sort order for these grades is not correct for sorting by difficulty. The comparator I implemented references a HashMap with the correct sort order for climbing grades. Pretty dang cool if you ask me!

## Uniform Exception Handling
In my opinion, no API is complete without stellar exception handling that returns error responses in a clear and uniform format. This API uses the following Error model (modeled after Microsoft's API standards) to return a uniform error response for any expected and unexpected errors that arise:

```
{
  "apiErrorCode": "string", // Human-readable, server-defined error code representing the overall error class.
  "message": "string", // The description of the overall error.
  "errorDetails": [ // A list of the actual individual errors
    {
      "code": "string", // Human-readable, server-defined error code representing the specific error.
      "target": "string", // The part of the request that caused the error. Could be a field in the request body, path variable, or query param.
      "description": "string" // A description of the specific error.
    }
  ]
}
```

I have created several custom exceptions for notifying a Global Exception Handler that returns error responses in this format.

## Sring Security using JSON Web Tokens
One of the coolest things about this API is that it is secured using Spring Security 6 and JSON Web Token authentication. This means that in order to access any of the secured endpoints of this API, a valid JSON Web Token must be sent in the Authorization header of the request. Here is a nice diagram that illustrates how the authentication works:

![image](https://github.com/KyleRobison15/krd-lets-climb-rest/assets/81257957/ff40f1ea-edfa-4579-9893-046444d7b408)

To construct the architecture shown above, I implemented the following:
### SecurityConfig
The SecurityConfig class is where I defined the Spring Beans required by Spring Security for authenticating requests to our API. The most important of them being the SecurityFilterChain bean, which utilizes the chain of responsiblity pattern to chain together security filters that should be executed for each request. The JwtAuthenticationFilter is added to this filter chain in order to validate the JWT provided in the request header.

### CustomUserDetailsService
A UserDetailsService is an interface that essentially tells Spring Security how to look up a User from your database. For this API, I created a CustomUserDetailsService concrete implementation of this interface, which overrides the loadUserByUsername method to look up a user by username from the letsclimbdb MySQL database.

### JwtService
The JwtService is a service class that can generate, validate and extract claims from JWTs. When a user registers via the /api/v1/auth/register endpoint, this service is called to generate and return a JWT to the newly registered user.

### JwtAuthenticationFilter
The JwtAuthenticationFilter is an additional filter I added to the SecurityFilterChain that, when executed, is responsible for extracting the JWT from the request's auhtorization header, extracting the user's username from the JWT using the secret signing key that the JWT was created with, and finally update the security context holder with the authenticated user's UserDetails.  Once this filter is executed, the request will either be rejected with a 403 forbidden response, or the request will be authenticated and the validated user information will be added to the security context.

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
