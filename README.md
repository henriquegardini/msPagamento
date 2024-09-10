# Payment microservice


## Description

The microservice **msPagamento** is developed in Java and Spring Boot. It simulates functions of payments of ShoppingCarts and, through jwt tokens, assures access and control to microservice's resources in a secure way.

## Funcionalities

- **Payment Order creation:** Creation of payment orders.
- **Payment process:** This simulates the payment. Since it's only for educational purposes, the success of a payment it's random and has 85% chance of being completed.
- **Payment updates:** It's possible to update payment's method and statuses(through previous functionality).
- **PIX "Copia e Cola" generator:** msPagamento generates automatically the "PIX Copia e Cola" code.

## Requisitos

- Java 21+
- Spring Boot 3.x
- Maven 3.x

## Estrutura do Projeto

- **Controller:** HTTP management layer.
- **Service:** Business logic layer - includes validation such as states transitions, due dates, input data.
- **Security:** Request validations - authorization and authentication through jwt tokens .
- **Repository:** Database access through JPA.
- **Exception Handling:** Global handling of exception to maintain clean code and information to user.

## Configuration

1. Clone the repository:

   ```bash
   git clone https://github.com/henriquegardini/msPagamento.git
    ```
   
   - **1.1 Maven native Option**
     - **1.1.1** Access the project folder:

       ```bash
       cd msPagamento
       ```

     - **1.1.2** Install Maven dependencies:

        ```bash
        mvn clean install
        ```
     - **1.1.3** Configure database at application.properties/application.yml.

     - **1.1.4** Run application:

       ```bash
       mvn spring-boot:run
       ```
   - **1.2 Docker Compose** _<small>(docker and docker compose are needed at the machine)</small>_
     
     - **1.2.1** Access the project folder:

        ```bash
        cd msPagamento
        ```

     - **1.2.2** Run docker compose:

        ```bash
        docker-compose up
        ```


## Use

### Some available endpoints:
- **POST /payment:** Creates a payment order. Returns the payment order created - if valid -.
- **POST /payment/{paymentId}/process:** Process payment order and returns if valid.
- **GET /payment/{paymentId}:** Returns payment with id = paymentId.
- **DEL /payment/{paymentId}:** Deletes payment with id = paymentId.

  ### JSON Collection:
      For user convenience, there is a JSON collection inside the project with all requests that can be done.
      It's using environment variables to facilitates the requests being done.
      For example, the first request creates the user at msLogin, then the second request it's already 
      configured to use login and password from the first request.
      Another example is that when a payment order is created, the id is saved at the colletionsVariables and
      can will be used automatically at other requests until a new payment order is created.