# Weatherman application for CGI  

This is a weatherman application made for CGI summer internship. 

I assumed that the calls to third party APIs were to be made from the Spring Boot backend. 

Unfortunately I didn't have time to figure out how to deploy the project. For testing purposes you can run the code following these steps:  
First run the docker MongoDb database using the `docker compose -f docker-compose.yaml -d` command in the backend folder.  
Then run the Spring Boot backend from an IDE of your choice. (Ayou have to add an environment variable `API_KEY` with an API key from WeatherAPI toyour spring boot app.   
Then run the frontend using the `ng serve` command in the frontend folder.  
Now you can test the app on localhost:4200 from the browser. 

I started the process of creating this app with researching both Angular and Spring Boot. I read some blog posts, watched some tutorial 
videos and read some of the documentation. Then I created the backend with [Spring initializer](https://start.spring.io/) and the frontend with the [Angular cli](https://angular.io/cli). 
Then I started implementing the backend testing it using Postman. When the backend could send a response with data from 2 weather APIs I started implementing the frontend. 
Then slowly I added more functionality and foxed many bugs along the way. 
The frontend took the most time because I had more experience beforehand with Java than Javascript/typescript. 

I mostly just added features in the order they were in the document because I felt that that was the most logical order to me. Unfortunately I didn't have time to implement all of the features. 

The biggest challenge for me was using Angular and typescript in general because I had no experience with them before the project. I had only used a little Javascript before. 
But I found a lot of useful information on Angular on the internet. There were lots of blog and forum posts and YouTube videos on it. 

I started implementing the feature that lets the user compare the current weather to the predictions saved in the database. 
Unfortunately I didn't finish it. It could be implemented by editing the endpoint that returns saved predictions from the database to also take the location as a parameter. 
Then you would need to create an endpoint that returns current weather info from the apis. 
Then add an option to the frontend to see the current weather. And then add an option to request the saved data about the location from the database. 
I am not sure about the automatic saving of predictions as I did not have time to look into it at all. 

I learned a lot from implementingthe project. I learned ho to create an Angular app aswell as a Spring boot app. I also learned a lot about typescript and Angular: the consepts of components and services, typescript syntax, interfaces and so on. 
I also learned how to add a map to your application(altho the placement of the map is still a bit messy and bugged). 
I learned how to use 2 third party weather APIs and how to use Spring boot and how to send http requests from the spring backend. 
