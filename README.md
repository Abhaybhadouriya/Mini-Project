# Getting Started with Mini Project With Abhay Bhadouriya
---

## Project Overview  

This application is designed to manage a **Student Billing System**. It allows students to log in, view their due bills, and make payments in multiple installments while tracking the total payments made. The following functionalities are implemented to achieve this:  

### Core Functionalities  
1. **Student Login:**  
   Students can log in securely using their email and password. Upon successful authentication, they gain access to their personalized billing dashboard.  

2. **View Due Bills:**  
   After logging in, students can view all their outstanding bills, including details like bill amount and due dates.  

3. **Make Payments:**  
   Students can pay their bills either in full or in multiple installments. Each payment is recorded and tracked until the total bill amount is paid.  

4. **Track Payment History:**  
   The system keeps a record of all payments made by the student, providing transparency and accountability.  

---
### This project is divided in 2 section
#### 1. Front End
#### 2. Back End

## Teck Stack used
#### Front End
##### 1. HTML
##### 2. CSS
##### 3. JAVASCRIPT
##### 4. ReactJS
##### 5. BootStrap
#### Back End
##### 1. SpringBoot Framework
##### 2. MySQL


---

## Authentication Mechanism

This application uses **JWT (JSON Web Token)** for authentication. Here's how it works:

1. **JWT Token Supply:**  
   Every request made to the server must include the JWT token in the `Authorization` header in the following format:  
   ```
   Authorization: Bearer <your-jwt-token>
   ```

2. **Token Interception and Validation:**  
   The server intercepts each request and validates the provided JWT token. If the token is:
   - **Valid**: The request is allowed to proceed to the intended resource or endpoint.
   - **Invalid or Missing**: The request is rejected with an appropriate error response.

This ensures secure and stateless authentication for all server interactions.

---
---


## API Endpoints  

The following APIs are used to implement the above functionalities:  

### Authentication  
1. **Login API**  
   - **Endpoint:** `POST /api/auth/login`  
   - **Purpose:** Allows students to log in using their email and password. Returns a JWT token upon successful login for secure access to the portal.  
   - **Request Body:**  
     ```json
     {
       "email": "",
       "password": ""
     }
     ```  

2. **Validation API**  
   - **Endpoint:** `POST /api/auth/checkValidation`  
   - **Purpose:** Verifies the validity of the JWT token to maintain secure communication.  

---

### Billing Management  
1. **View Unpaid Bills**  
   - **Endpoint:** `POST /api/students/unpaidbills`  
   - **Purpose:** Fetches a list of all due bills for a logged-in

This section provides a clear and well-organized overview of the API functionality for your application.

   - **Request Body:**  
     ```json
     {
       "studentId": 0
     }
     ```  

2. **View Paid Bills**  
   - **Endpoint:** `POST /api/students/paidbills`  
   - **Purpose:** Retrieves a list of all bills that the student has fully paid.  
   - **Request Body:**  
     ```json
     {
       "studentId": 0
     }
     ```  

3. **View All Bills**  
   - **Endpoint:** `POST /api/students/viewbills`  
   - **Purpose:** Provides a comprehensive view of both paid and unpaid bills for the student.  
   - **Request Body:**  
     ```json
     {
       "studentId": 0
     }
     ```  

4. **Pay Fees**  
   - **Endpoint:** `POST /api/students/payFees`  
   - **Purpose:** Allows students to make a payment towards their bills. Payments can be made in full or in installments.  
   - **Request Body:**  
     ```json
     {
       "bill_id": 0,
       "student_id": 0,
       "amount": {},
       "description": ""
     }
     ```  
   - **Behavior:**  
     - The `amount` field represents the payment installment.  
     - The system updates the bill's total due amount and tracks the installment history.  

---

### Student Registration and Miscellaneous  
1. **Student Registration**  
  
   - **Endpoint:** `POST /api/students/register`  
   - **Purpose:** Allows new students to register into the system by providing their details.  
   - **Request Body:**  
     ```json
     {
       "cgpa": 0,
       "domain": "",
       "email": "",
       "first_name": "",
       "last_name": "",
       "graduation_year": 0,
       "password": "",
       "photograph_path": "",
       "placement_id": 0,
       "roll_number": "",
       "specialisation": "",
       "total_credits": 0
     }
     ```  

2. **Test API**  
   - **Endpoint:** `POST /api/students/test`  
   - **Purpose:** Serves as a placeholder for testing server connectivity or other experimental operations.  

3. **Get Student by Email**  
   - **Endpoint:** `GET /api/students/{{email}}`  
   - **Purpose:** Fetches the details of a student using their email address.  

---

## API Workflow  

1. **Login Process:**  
   - The student uses the `POST /api/auth/login` API to log in with their credentials.  
   - Upon successful login, a JWT token is issued, which must be included in the `Authorization` header as a Bearer token for all subsequent requests.  

2. **View Bills:**  
   - The student can use the `POST /api/students/unpaidbills` and `POST /api/students/paidbills` APIs to view their due and cleared bills, respectively.  

3. **Make Payments:**  
   - Using the `POST /api/students/payFees` API, the student can make a partial or full payment toward a specific bill.  

4. **Track Payment History:**  
   - The `POST /api/students/viewbills` API provides a summary of both paid and unpaid bills, helping the student monitor their payment progress.  

5. **Registration:**  
   - New students can register using the `POST /api/students/register` API, enabling them to create an account and access the billing portal.  

---

This detailed explanation aligns the API functionality with your problem statement, demonstrating how each endpoint contributes to the solution.

Here’s a concise documentation for your React.js front-end:

---

## Front-End Documentation  

### Overview  
The front-end of this project is built using **React.js** to create a dynamic and responsive user interface for the Student Billing System. The UI allows students to log in, view their due bills, make payments, and track their payment history.  

---

### Key Features  
1. **User Authentication**:  
   - Login page for students to authenticate using their credentials.  
   - Secure JWT-based token storage in the browser (localStorage/sessionStorage).  

2. **Dashboard**:  
   - Displays due and paid bills fetched from the back-end API.  
   - Provides options for making payments and viewing payment history.  

3. **Payment Processing**:  
   - Allows students to enter installment amounts and descriptions.  
   - Validates payment inputs before submitting to the back-end.  

4. **Responsive Design**:  
   - Built with **Bootstrap** for mobile-friendly and visually appealing layouts.  

---
Here’s an updated documentation section based on your directory structure:

---

## Front-End Directory Structure  

The project follows a modular structure to maintain separation of concerns, making the application scalable and easy to maintain. Below is the directory overview with brief descriptions:

```
src/
├── auth/  
│   ├── GetData.js              # Utility to fetch data from APIs.  
│   ├── RequestAuth.js          # Handles authentication-related API requests.  
│   └── Validatetoken.js        # Validates the JWT token for secure API access.  
│  
├── components/  
│   ├── footer/  
│   │   └── Footer.jsx          # Footer component for the application.  
│   ├── header/  
│   │   ├── header.css          # Styles for the header.  
│   │   └── Header.jsx          # Header component with navigation and branding.  
│   └── login/  
│       ├── Login.css           # Styles specific to the login page.  
│       └── Login.jsx           # Login form component.  
│  
├── mainScreen/  
│   └── Home.jsx                # Main screen component displayed after login.  
│  
├── paymentpage/  
│   └── AllTransactions.jsx     # Component for listing all transactions.  
│  
├── paymentHistory/  
│   └── PaymentHistoryPage.jsx  # Component displaying the payment history of a student.  
│  
├── viewBills/  
│   ├── BillPayments.jsx        # Component for displaying a list of bills.  
│   └── PayPartially.jsx        # Component to process partial bill payments.  
│  
├── App.css                     # Global application styles.  
├── App.js                      # Root component that integrates all routes and components.  
├── App.test.js                 # Test cases for the application.  
├── index.css                   # Global CSS for shared styles.  
├── index.js                    # Entry point for the application.  
├── logo.svg                    # Application logo.  
├── reportWebVitals.js          # Performance monitoring setup.  
└── setupTests.js               # Configuration for testing utilities.  
```

---

### Key Directories  

1. **`auth/`**  
   - Contains authentication-related utilities, such as fetching and validating tokens and handling secure API requests.  

2. **`components/`**  
   - Contains reusable components organized by their specific functionality (e.g., `header`, `footer`, `login`).  

3. **`mainScreen/`**  
   - Contains the home screen (`Home.jsx`), which serves as the dashboard for logged-in students.  

4. **`paymentpage/`**  
   - Includes components for managing transactions, such as listing all student payments.  

5. **`paymentHistory/`**  
   - Handles displaying the payment history of students for tracking installment records.  

6. **`viewBills/`**  
   - Contains components for viewing bills (`BillPayments.jsx`) and making partial payments (`PayPartially.jsx`).  

---

### Navigation Flow  

1. **Login Flow**:  
   - The `Login.jsx` component handles user authentication and redirects to the home screen (`Home.jsx`) upon success.  

2. **Bill Management**:  
   - `BillPayments.jsx` displays unpaid bills.  
   - `PayPartially.jsx` processes installment payments and updates the bill status.  

3. **Payment History**:  
   - `PaymentHistoryPage.jsx` shows all past transactions for the student, providing detailed payment history.  

4. **Reusable Components**:  
   - The `Header.jsx` and `Footer.jsx` components are used across the application for consistent navigation and branding.  

---
---

### Key Dependencies  
- **React**: Framework for building UI components.  
- **React Router**: For navigation and routing between pages.  
- **Fetch**: For making HTTP requests to the back-end API.  
- **Bootstrap**: For responsive design and pre-styled components.  

---

### How It Works  
1. **Authentication Flow**:  
   - The login form sends user credentials to the `/auth/login` API.  
   - Upon success, the JWT token is stored in the browser for subsequent requests.  

2. **Bill Management**:  
   - The dashboard fetches unpaid bills (`/students/unpaidbills`) and displays them in a table.  
   - Paid bills can be fetched and displayed separately.  

3. **Payment Functionality**:  
   - Payments are processed through the `POST /students/payFees` API.  
   - The front-end validates inputs (e.g., amount > 0) before sending the request.  

4. **Token Interception**:  
   - Fetch interceptors are used to include the JWT token in all outgoing API requests.  

---

### Running the Front-End  

1. **Install Dependencies**:  
   ```bash
   npm install
   ```  

2. **Start the Development Server**:  
   ```bash
   npm start
   ```  

3. **Build for Production**:  
   ```bash
   npm run build
   ```  

---

Here’s a concise documentation section for your **Spring Boot back-end**, based on the provided directory structure:

---

## Back-End Documentation  

The back-end is built using **Spring Boot** to provide a secure and efficient REST API for the Student Billing System. It implements JWT-based authentication, bill management, and payment tracking functionalities.

### Project Directory Structure  

```
src/
├── main/
│   └── java/
│       └── com/abhay/
│           ├── configuration/
│           │   └── SecurityConfig.java             # Security configuration for JWT authentication.  
│           ├── controller/
│           │   ├── AuthenticationController.java   # Handles user login and token generation.  
│           │   └── CustomerController.java         # Manages student-related operations (e.g., viewing bills, payments).  
│           ├── dto/
│           │   ├── BillWithPayments.java           # Data transfer object for bill details with payment information.  
│           │   ├── CustomerRequest.java            # DTO for student registration requests.  
│           │   ├── CustomerResponse.java           # DTO for student information responses.  
│           │   ├── FeesPayment.java               # DTO for fees payment data.  
│           │   ├── LoginRequest.java              # DTO for login credentials.  
│           │   ├── PaidBills.java                 # DTO for representing paid bills.  
│           │   ├── StudentBillViewRequestDTO.java # DTO for viewing student's bill details.  
│           │   └── UnpaidBills.java               # DTO for representing unpaid bills.  
│           ├── entity/
│           │   ├── Bill.java                      # Entity representing a bill in the system.  
│           │   ├── Customer.java                  # Entity representing a student.  
│           │   ├── StudentBill.java               # Entity for a student's bill information.  
│           │   └── StudentPayment.java            # Entity for student payment details.  
│           ├── exception/
│           │   └── CustomerNotFoundException.java # Custom exception for handling cases where a student is not found.  
│           ├── helper/
│           │   ├── EncryptionService.java         # Utility for encrypting sensitive data.  
│           │   ├── JWTHelper.java                # Helper class for generating and validating JWT tokens.  
│           │   └── RequestInterceptor.java        # Intercepts incoming requests for validating JWT token.  
│           ├── mapper/
│           │   └── CustomerMapper.java           # Mapper to convert DTOs to entities and vice versa.  
│           ├── repo/
│           │   ├── BillRepo.java                 # Repository interface for bill-related database operations.  
│           │   ├── CustomerRepo.java             # Repository interface for student-related database operations.  
│           │   └── StudentPaymentRepo.java       # Repository interface for payment-related database operations.  
│           ├── service/
│           │   └── CustomerService.java          # Service layer for business logic related to student management.  
│           └── MiniProjectApplication.java      # Main Spring Boot application entry point.  
```

### Key Components  

1. **Security Configuration** (`SecurityConfig.java`)  
   - Configures Spring Security to secure the application with JWT-based authentication.
   - Ensures that all endpoints are protected and only accessible with a valid token.

2. **Controllers**  
   - **`AuthenticationController.java`**: Manages login functionality, including authenticating users and issuing JWT tokens.  
   - **`CustomerController.java`**: Handles student-related actions, such as registering, viewing bills, and making payments.

3. **DTOs (Data Transfer Objects)**  
   - Used to encapsulate data in a structured format for API requests and responses. Examples:
     - **`CustomerRequest.java`**: Represents the student registration request.  
     - **`FeesPayment.java`**: Represents the data for making fee payments.  
     - **`BillWithPayments.java`**: Represents bills along with their payment status.

4. **Entities**  
   - **`Bill.java`**: Entity for a bill record, with attributes such as `amount`, `dueDate`, and `status`.  
   - **`Customer.java`**: Entity for student details, including `name`, `email`, and other personal data.  
   - **`StudentPayment.java`**: Entity for tracking individual payments made by students.

5. **Repositories**  
   - **`BillRepo.java`**, **`CustomerRepo.java`**, and **`StudentPaymentRepo.java`**: Interfaces for interacting with the database to perform CRUD operations on `Bill`, `Customer`, and `StudentPayment` entities.

6. **Services**  
   - **`CustomerService.java`**: Contains the business logic for managing students, bills, and payments.
   
7. **Helpers**  
   - **`JWTHelper.java`**: Provides methods to generate and validate JWT tokens used for authentication.  
   - **`RequestInterceptor.java`**: Intercepts incoming HTTP requests to extract and validate JWT tokens in the header.

8. **Exception Handling**  
   - **`CustomerNotFoundException.java`**: Custom exception that is thrown when a student cannot be found during any operation.

---

