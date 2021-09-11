# cinema-app

### Tech stack

1. Springboot
2. Kotlin
3. Gradle
4. Docker
5. Swagger
6. Jpa
7. postgres

### System requirements:

1. Java 11
2. Docker installed and running

### Running the application:

1. /swagger-ui holds more detailed documentation for the API

2. The movies are being loaded directly to the database from the endpoint when the app is started. Add your key in
   the variable API_KEY in the class src/main/kotlin/com/example/cinema/service/rest/MovieRestService.kt. If this
   step is skipped, the app will not run.

3. In the root folder of this project, run:

````
./gradlew bootJar
````

this will build the jar file necessary to run the application.

4. Create and run the docker container:

````
docker-compose up (add -d if you want detached mode)
````

5. The application should be available In Port:

````
8080
````

6. If you want to redeploy the application with any code change:

````
1. ctrl + c (if you are not in detached mode)
2. docker-compose down
3. docker rmi cinema-app
Back to step 1 (Don't forget to rebuild your .jar if changes are made)
````

7. For development:

It is not the best option to run docker every time you make a change, so you can this as another option:

* Run:

````
docker-compose -f docker-compose-local.yml up (-d if you want detached mode)
````

* Change file under src/main/resources/application.properties

````
spring.datasource.url=jdbc:postgresql://db:5432/cinemadb
spring.flyway.url=jdbc:postgresql://db:5432/cinemadb
for:
spring.datasource.url=jdbc:postgresql://localhost:5432/cinemadb
spring.flyway.url=jdbc:postgresql://localhost:5432/cinemadb
````

* Run the application from the IDE or any other method, it will run the application and will communicate with the
  database.

* To stop the database:

````
ctrl+c (if you are not in detached mode)
docker-compose -f docker-compose-local.yml down
````

### Using the application:

#### Available endpoints

1. All calls need an auth header:
   Basic Authorization:

````
username: "user" for normal user or "admin" for ADMIN user
password: password
````

These credentials are set up for the users added by default in the database, if not set, it will throw 401 error.

2. Get all movie shows (it is important to get the movie ids)

* get movie/show expected response:

````
{
    "movies": [
        {
            "movieId": 17,
            "movieName": "The Fast and the Furious",
            "shows": [
                {
                    "roomId": 2,
                    "roomName": "NORMAL_3D",
                    "movieTime": "15:40",
                    "price": 20.10
                }
            ]
        },
        {
            "movieId": 19,
            "movieName": "The Fast and the Furious: Tokyo Drift",
            "shows": []
        }
    ]
}
````

3. Create a movie show (meaning a time, price and room for determined movie):
   This endpoint just work using the admin user

* post /movie/show: expected body

````
{
    "movieId":"19",
    "room": any of ["NORMAL", "VIP", "VIP_3D", "NORMAL_3D"],
    "time":"19:00", (Follow this pattern, 24h)
    "price": 27
}
````

It will update the data if you send the same combination on movie and room.

4. Delete a movie show:
   This endpoint just work using the admin user

* delete movie/show: expected body:


5. Get movie information:

* get movie/{movieId} expected response

````
{
    "id": "tt0232500",
    "title": "The Fast and the Furious",
    "description": "Los Angeles police officer Brian O'Conner must decide where his loyalty really lies when he becomes enamored with the street racing world he has been sent undercover to destroy.",
    "releaseDate": "22 Jun 2001",
    "imdbRating": 6.8,
    "runtime": "106 min",
    "ratings": [
        {
            "source": "Internet Movie Database",
            "value": "6.8/10"
        },
        {
            "source": "Metacritic",
            "value": "58/100"
        }
    ],
    "reviewsOnCinema": 2.6666666666666665,
    "commentsOnCinema": [
        "Good movie",
        "Bad movie",
        "Excelent movie"
    ]
}
````
reviewsOnCinema and commentsOnCinema are values from the users that add their reviews to the system. The other field are got from the endpoint.

6. Add a review on a movie:

* post /review expected body

````
{
    "movieId":"17",
    "comment":"Bad movie",
    "rate":1 (Number between 1 of 5)
}
````

7. Add a reservation for tickets:
* post /reservation: expected body
````
{
    "movieId":17,
    "roomId":2,
    "numberOfTickets":4
}
````

It will send this response:

````
{
    "movieName": "The Fast and the Furious",
    "tickets": 4,
    "room": "NORMAL_3D",
    "totalPrice": 80.40 Calculated total value with the value of each ticket
}
````

````
{
    "type":"any of these ["TEAMVIEWER","ANTIVIRUS","CLOUDBERRY","PSA"]"
}
````

#### Tech debt

1. Persistence layer is not tested, integration tests should be added there.
2. Improve error management to show better errors to client.
3. User administration