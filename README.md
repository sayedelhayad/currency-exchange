# Currency Exchange and Discount Calculation

## Description
This is a Spring Boot application that integrates with a ExchangeRate client which is currency
exchange API to retrieve real-time exchange rates. The application calculates the total
payable amount for a bill in a specified currency after applying applicable discounts. The
application exposes an API endpoint that allows users to submit a bill in one currency
and get the payable amount in another currency.


---
## There is UML file exists on the UML folder on the root application directory.

## Table of Contents

- [Installation](#installation)
- [Running](#running)
- [Testing](#testing)
- [Generate Coverage Report](#generate-coverage-report)
- [View the Coverage Report](#view-the-coverage-report)
- [Usage](#usage)

---
## Installation

Navigate to the project directory and run:

```bash
mvn clean install
```

## Running 

Navigate to the project directory and run:

```bash
mvn spring-boot:run
```

## Testing 

Execute the tests using the following command:

```bash
mvn clean test
```

To manual test the application, the project initilized by testing data:
There are two usernames ('emp' and 'cus') and the password is '123'.


## Generate Coverage Report:

Execute the following command:

```bash
mvn jacoco:report
```

## View the Coverage Report:

The coverage report will be generated in the target/site/jacoco directory. You can open index.html in a web browser to view the report.

## Usage 

- 1- Use the signup API to register a user.
- 2- Use the signin API to login and get authentication token.
- 3- Use the authentication token ass a Bearer token while calling the calculate API.

