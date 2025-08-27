# Library API Assignment

A school project built in Java, featuring a simple library REST API. Using Spring Boot, Spring Web, Spring Data JPA,
SQLite and Maven

## Features

- Get, search and create books. With pagination, sorting and filtering.
- Get, search and create authors.
- Get and create users.
- Get user loans.
- Create, return and extend loans
- Unit & integration tests for loans (mainly creating them)

## Notes

- Manually managed JOINs, because of queries being ran N+1.
- No password hashing (not part of the course)