# Product Service

This is a web service written in Java using spring boot. It uses Postgres as database.

This service has following functionalities:

* Creating, Updating and Deleting products
* Retrieving products of certain category with pagination

## Install and Run the Project

### Pre-requests

* [Docker](https://docs.docker.com/get-docker/) and [Docker-Compose](https://docs.docker.com/compose/install/) is a
  pre-request to run the application using docker.
* [Postgres](https://www.postgresql.org/download/) is a pre-request if you want to run the application on your machine.
* Java and Maven is required to create jar file
* Postgres must have the database named as ```product```. You can follow
  this [link](https://www.postgresql.org/docs/9.0/sql-createdatabase.html) to create a new database on postgres
* You can connect to a remote database by editing ```application.properties``` file
### Install the source code

* Download the [zip](https://github.com/yusufTopcuoglu/e-commerce/archive/refs/heads/master.zip) or clone the project.

### Run the project using docker

* Run ```./mvn clean package``` to create jar file.
* Run ```docker-compose up``` command

### Run the project using maven and Java

* Run ```./mvn clean package``` to create jar file.
* Run ```java -jar target/product-service-0.0.1-SNAPSHOT.jar``` to run the jar file.

## Example API Usage
* Create a new product:
``` 
POST http://localhost:8080/product/create 
Body raw (JSON)
  {
  "name": "product_name",
  "price": 1,
  "category": "ELECTRONIC",
  "imageLink": "image_link"
  }
```

* Update existing product
``` 
POST http://localhost:8080/product/update 
Body raw (JSON)
  {
  "id": 1
  "name": "product_name",
  "price": 1,
  "category": "ELECTRONIC",
  "imageLink": "image_link"
  }
```

* Get product of certain category
```
GET http://localhost:8080/product?productCategory=HOME&page=0&count=1
```


## Useful Tools
* You can use [Postman](https://www.postman.com/downloads/) to send http requests and experiment on the project
* You can use [pgAdmin](https://www.pgadmin.org/download/) to monitor the database