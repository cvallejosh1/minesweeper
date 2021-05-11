# MineSweeper REST API
Repository for minesweeper challenge

The project is based on Spring Boot 2.x, Java 11 and MySQL database.

It provides methods to interact with the minesweeper game.

### Using the API locally or with docker ###

You can run it locally or can use docker setup using the following steps:

1. Clone the repository
2. Go to docker_setup directory
3. Execute the setup.sh script and then wait until completion.
4. You can then verify if two containers were created.

    - minesweeper-mysql-container --> For MySQL
    - minesweeper-api-container --> For the REST API
    
5. The project also includes unit tests covering almost 100% of the cases.

Then after running with docker setup you can access to the Swagger documentation.

http://localhost:8180/minesweeper/api/swagger-ui.html

### Using the API in the cloud

The API is also available on AWS in the following URL you can access the API documentation.

http://ec2-18-217-182-106.us-east-2.compute.amazonaws.com/minesweeper/api/swagger-ui.html

In the cloud version the API is built in a docker container and then deployed into AWS
using AWS ECR and AWS ECS. 

The database is MySQL using AWS RDS (t2.micro) and using AWS Secrets Manager 
for the DB credentials. 

The aws profile is the one related to AWS configuration.

** The project does not consider security and authentication but it can be implemented
using a third provider like AWS Cognito.
    
