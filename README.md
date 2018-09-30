# Multithreaded pooled Web Server

## Introduction
#### Contains the code in Java for a VERY SIMPLE multithreaded ( pooled ) web server, that has the following characterstics : 
1. Only **GET** method is allowed. ( for simpilcity )
2. Other HTTP Methods return a **405 Method Not Allowed** response.
3. Only files in the **src/resources** directory are served - WEB ROOT. If not found, returns a **404 Not Found** response.
4. Exceptions will return a **500 Internal Server Error** ( _Check logs for more details_ )
5. There is scope of **422 - Unprocessable entity** ( for future )
6. There is scope for serving complex resources if needed ( For future )
7. Default content type that's served is _text/html_ for simplicity purposes
8. _Lots of fats have been cut for the sake of brevity_

## How to run ?
1. Run the application from the App.java class
2. Visit [http://localhost:9001](http://localhost:9001) or [http://localhost:9001/index.html](http://localhost:9001/index.html) ( Default port )
3. Try adding a new HTML or Text File under **src/resource** directory and test

## Contribution
Please feel free to contribute
