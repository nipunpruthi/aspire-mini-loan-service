# Aspire Mini Loan Service

## Introduction
This service is used to manage loan application from customers and collect emi from customers

## Postman Collection
Import file `Aspire-mini-loan-service.postman_collection.json` into Postman

## Best Experience : Use Intellij Idea Ultimate

## Requirements 

### Functional Requirements
1. Allow customer to request for new Loan 
2. Allow admin to view and approve/decline Loan 
3. Submit weekly loan repayments 
4. Generate repayments and PENDING state
5. Policy to make sure customer can view his own loan only
6. Policy to verify amount is greater than or equal to scheudled payment
7. Loan become paid is whole amount is paid

### Additional Features
1. Appropriate exception and error code with message for wrong or not allowed requests
2. Authentication before login


### Auth Details
3. The user authentication details are saven in-mem in `SecurityConfig.java` for prototyping

## Commands

build : `./gradlew clean build`
test : `./gradlew clean test`
run : `./gradlew clean run`


## APIs

| API                        | Type  | Description                                     | Auth User |
|----------------------------|-------|-------------------------------------------------|-----------|
| `/api/v1/loan`             | get   | Get All loans by the authenticated user         | Customer  |
| `/api/v1/loan`             | post  | Apply for new loan                              | Customer  |
| `/api/v1/loan/plan`        | get   | Get upcoming Installment of a loan              | Customer  |
| `/api/v1/loan/plans`       | get   | Get All Installment(past+upcoming) of a loan    | CUSTOMER  |
| `/api/v1/loan/plan`        | patch | Pay EMI                                         | CUSTOMER  |
| `/api/v1/admin/loan`       | get   | get All Loans of all Users                      | ADMIN     |
| `/api/v1/admin/loan`       | patch | Approve or reject loan                          | ADMIN     |
| `/api/v1/admin/loan/plans` | get   | get all past and upcoming installment of a loan | ADMIN     |
| `/api/v1/admin/loan`       | patch | Pay EMI                                         | ADMIN     |


## Test Coverage
Command to run all the tests(20+ tests) : `./gradlew clean test`

### API Test Coverage
* AdminLoanControllerApiTest.java
* CustomerLoanControllerApiTest.java

### Service Unit Test Coverage
* LoanServiceUTest.java

### Integration Test
* InstallmentRepositoryITest.java
* LoanRepositoryITest.java

## Database
type : `h2` in-memory embedded database
Note : First I used `postgres`, but to make the application run simpler, migrated to `h2` embedded db

### Schemas

loan

| column               | Type    | Description          |  
|----------------------|---------|----------------------|
| `id`                 | UUID    | Loan ID(PK)          |
| `customer_id`        | varchar | ID of Customer       | 
| `amount`             | float8  | Loan Amount          |
| `loan_term`          | int4    | Loan Term            | 
| `installment_amount` | float8  | Installment Amount   | 
| `pending_amount`     | float8  | Total Pending Amount | 
| `pending_terms`      | int4    | Pending Terms        | 
| `status`             | int2    | Status of Loan       | 
| `loan_applied_date`  | date    | Loan Applied Date    | 
| `loan_approved_date` | date    | Loan Approved Date   | 
| `updated_by`         | varchar | Loan Approved By     | 
---------------

installment


| column                   | Type    | Description          |
|--------------------------|---------|----------------------|
| `id`                     | UUID    | Loan ID(PK)          |
| `actual_payment_date`    | varchar | ID of Customer       | 
| `loan_id`                | float8  | Loan Amount          | 
| `pending_amount`         | int4    | Loan Term            |
| `received_amount`        | float8  | Installment Amount   | 
| `scheduled_payment_date` | float8  | Total Pending Amount | 
| `payment_status`         | int4    | Pending Terms        | 
| `term_no`                | int2    | Status of Loan       | 




