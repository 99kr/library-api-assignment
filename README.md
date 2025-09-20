# library-api-assignment

A school project for the courses Java Spring and Säker mjukvara ("Secure Software").

## Table of Contents

1. [About](#library-api-assignment)
2. [Features](#features)

    2.1. [Backend](#backend)

    2.2. [Frontend](#frontend)

3. [Screenshots](#screenshots)

    3.1. [Home page](#home-page)

    3.2. [Authentication](#authentication)

    3.3. [Books](#books)

    3.4. [Logs](#logs)

    3.5. [Error](#error)

4. [Future Improvements & Notes](#future-improvements--notes)

## About

A school project (spanning 2 courses) built in Java, featuring a simple library REST API. Using Spring Boot, Spring Web, Spring Data JPA,
SQLite and Maven.

Featured with a basic dashboard-like frontend besides the assignment, using React, TypeScript, React Router, SWR, TailwindCSS and shadcn/ui.

Assignment criterias can be found here:

-   [Java Spring](https://github.com/THS24-Spring-Boot/Spring-Boot-Start/blob/main/projekt/kravspec-bibblan.md)
-   [Säker mjukvara](https://github.com/nilsedgarjacobsen/THS24-sakermjukvara/blob/main/spring_security_exam.md)

## Features

### Backend

-   Authentication using JWT (access and refresh tokens)
-   Authorization with roles
-   Audit logging involving important user actions
-   Get, search and create books. With pagination, sorting and filtering.
-   Get, search and create authors.
-   Get and create users.
-   Get user loans.
-   Create, return and extend loans
-   \*Unit & integration tests for loans (mainly creating them)

> \*Controller tests are currently broken, as the the tests are not up to date with the current code regarding authentication and authorization.

### Frontend

-   Automatic handling of JWT refresh tokens when access token expires
-   Login, logout and register
-   Paginated view of all books
-   Overview of JWT information for debug & development purposes
-   Abiltity to create books
-   Barebones page for viewing audit logs

## Screenshots

### Home page

#### As a guest

![Screenshot of the home page as a guest](docs/home-page-guest.png)

#### As a user

![Screenshot of the home page as a user](docs/home-page-user.png)

#### As an admin

![Screenshot of the home page as an admin](docs/home-page-admin.png)

### Authentication

#### Register

![Screenshot of the register page](docs/register-page.png)

#### Login

![Screenshot of the login page](docs/login-page.png)

#### Logout

![Screenshot of the logout page](docs/logout-page.png)

### Books

#### Grid view of all books

![Screenshot of the books page](docs/books-page.png)

#### Create book

![Screenshot of the create book dialog](docs/books-create-dialog.png)

### Logs

### Table view of audit logs

![Screenshot of the logs page](docs/logs-page.png)

### Error

#### 403 Forbidden

![Screenshot of the 403 Forbidden error page](docs/403-forbidden-page.png)

#### 404 Not found

![Screenshot fo the 404 Not Found error page](docs/404-not-found-page.png)

## Future Improvements & Notes

Possible future improvements and concerns that I currently have with the project:

-   The frontend is currently not very user friendly. It is built as a dashboard-like application, and not separated into a user interface and an admin dashboard. Also not responsive.

-   The frontend basically contains nothing, just a picked few features to help while fulfilling [the criterias of the assigment](https://github.com/nilsedgarjacobsen/THS24-sakermjukvara/blob/main/spring_security_exam.md).

-   SWR hooks are also kind of messy, and would definitely benefit from being improved upon. I don't see any scalability with the current implementation with having a file for each endpoint.

-   I would say the frontend has no sense of modularity, no generic components on top of shadcn/ui, no reusability of e.g forms etc.

-   Requests are being sent before we had time to initially refresh the jwt token (which means there will be an initial 401 errored request on the first page load).

-   Barely any unit tests for the backend (which 1/3 is failing).

-   No unit tests for the frontend either.

-   [API Response class](backend/src/main/java/com/kr/libraryapiassignment/response/ApiResponse.java) could definitely be improved upon. It is currently a bit of a mess with being a mix of a normal class and a builder.

-   Decisions are not entirely thought through, with a lot of them being based on the course requirements and just getting it to work.

-   Backend currently using a SQLite database, as that was included into the assignment. Would be better off using a proper database like PostgreSQL or MySQL.
