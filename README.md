# Multithreaded pooled Web Server

## Introduction
#### Contains the code in Java for a VERY SIMPLE multithreaded ( pooled ) web server, that has the following characterstics : 
1. Only GET method is allowed.
2. Other HTTP Methods return a 405 Method Not Allowed response.
3. Only files in the src/resources directory are served. If not found, returns a 404 Not Found response.
4. Exceptions will return a 500 Internal Server Error
5. There is scope of 422 - Unprocessable entity ( for future )
6. There is scope for serving complex resources if neeed ( For future )
7. Default content type that's served is text/html for simplicity purposes
8. Lots of fats have been cut for the sake of brevity

## How to run ?
1. Run the application from the App.java class
2. Visit localhost:9001/index.html ( Default port )

## Contribution
Please feel free to contribute
