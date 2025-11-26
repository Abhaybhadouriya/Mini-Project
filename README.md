
# Student Billing System - Mutation Testing Edition

A secure, full-stack Student Billing System built with **Spring Boot (Backend) + ReactJS (Frontend).  
Features secure JWT authentication, bill viewing, partial payments, transaction history, and **high test quality guaranteed by PIT Mutation Testing**.

## Key Features

- Secure login with JWT authentication
- View unpaid bills and calculate remaining balance
- Make partial payments
- Complete transaction history
- High code quality ensured by **PIT Mutation Testing**

## Tech Stack

- **Backend**: Spring Boot (Java 17), Spring Security, JWT, Maven
- **Frontend**: ReactJS
- **Testing**: JUnit 5 + PIT Mutation Testing (v1.15.2)

## Prerequisites

Make sure you have the following installed:

- Java 17 (LTS) or higher
- Maven 3.8+ (or use the included `./mvnw` wrapper)
- Git
- Node.js & npm (for frontend – if running separately)

## Setup & Installation

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd Mini-Project
```

### 2. Navigate to Backend

```bash
cd BackEnd
```

### 3. Build the Project

This will download dependencies and compile the code:

```bash
./mvnw clean install
```

(Use `mvnw.cmd` on Windows)

## Running Tests

### 4.1 Standard Unit Tests (JUnit 5)

```bash
./mvnw test
```

Reports location:  
`BackEnd/target/surefire-reports/`

### 4.2 Mutation Testing with PIT

This project uses **PIT Mutation Testing** to ensure test suite strength.

Run full mutation coverage:

```bash
./mvnw org.pitest:pitest-maven:mutationCoverage
```

#### What PIT Does:
- Creates mutants by altering code (e.g. changing `>` to `<=`, removing conditions, etc.)
- Runs your tests against each mutant
- **Killed** = Test detected the fault (Good!)
- **Survived** = Test missed the fault → Improve your tests!

### 4.3 View Mutation Test Report

After completion, open the detailed HTML report:

```
BackEnd/target/pit-reports/index.html
```

Just double-click `index.html` in your browser (Chrome/Firefox/Edge recommended).

You’ll see:
- Mutation score
- Line coverage
- Survived mutants (with source code diff)
- Class-by-class breakdown

## PIT Configuration (Defined in pom.xml)

Mutation testing is focused on core logic for performance:

**Targeted Packages:**
```
com.abhay.service.*
com.abhay.helper.*
com.abhay.mapper.*
com.abhay.configuration.*
```

**Targeted Test Classes:**
- CustomerServiceTest
- JWTHelperTest
- EncryptionServiceTest
- CustomerMapperTest
- RequestInterceptorTest
- SecurityConfigTest

You can narrow scope for faster runs:

```bash
./mvnw org.pitest:pitest-maven:mutationCoverage -DtargetClasses=com.abhay.service.CustomerService
```

## Troubleshooting

| Issue                            | Solution                                                                 |
|----------------------------------|---------------------------------------------------------------------------|
| Tests fail immediately           | Mutation testing requires all unit tests to pass first → Run `./mvnw test` |
| "History file not found" warning | Normal on first run – ignore it                                           |
| Mutation testing is too slow     | Limit scope with `-DtargetClasses=` or `-DtargetTests=`                   |
| Report not opening               | Make sure you open `target/pit-reports/index.html (not the folder)     |

## Author

- Naval Kishore Singh Bisht (MT2024099)
- Abhay Bhadouriys (MT2024003)


