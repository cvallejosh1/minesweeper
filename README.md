# MineSweeper REST API
Repository for minesweeper challenge

The project is based on Spring Boot 2.x and MySQL database.

It provides methods to interact with the minesweeper game.

You can run it locally or can use docker setup using the following steps:

1. Clone the repository
2. Go to docker_setup directory
3. Execute the setup.sh script and then wait until completion.
4. You can then verify if two containers were created.

    - minesweeper-mysql-container --> For MySQL
    - minesweeper-api-container --> For the REST API
    
5. The project also includes unit tests covering almost 100% of the cases.

After running locally you can access to the Swagger documentation.

http://localhost:8180/minesweeper/api/swagger-ui/index.html

** The project does not consider security and authentication but it can be implemented
using a third provider like AWS Cognito.
    
