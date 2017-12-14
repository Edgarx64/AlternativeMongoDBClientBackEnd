# Alternative MongoDB Client

The application accepts the query in the form of a sql,
makes a selection from the MongoDB and returns the result.
The application start on 8080 port.

## Dependencies

1. Spring Boot v1.5.9
2. Swagger v2.7.0
3. JUnit v4.12
4. Mongo Java Driver v3.6.0

## Necessary
1. JDK 8
3. Maven
2. MongoDB

## How to install
1. Install JDK 8 [follow the link](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 
2. Download Maven [follow the link](https://maven.apache.org/download.cgi)
and Install [follow the link](https://maven.apache.org/install.html)

3. Manual installation MongoDB [follow the link](https://docs.mongodb.com/manual/installation/)
 
## Configuration
 Before running the application you need to execute `mongod`. For example, on Linux you need to execute the command:
 ```
 sudo start service mongod
 ```
 For macOS at the system prompt `mongod`, Windows - `mongod.exe`

## Mongo database for example

 Run the following code in the mongo command prompt
 
```
db.mycol.insert([
{
   a: 'text', 
   b: 10,
   c: ['one', 'two', 'three'],
   d: {da: 4, db: "14"}
},
{
   a: 'abc', 
   b: 20,
   c: ['one', 'two', 'three', 'four'],
   d: {da: 2, db: "22"}
},
{
   a: 'zxc', 
   b: 5,
   c: ['one', 'two', 'three', 'four', 'five'],
   d: {da: 1, db: "41"}
},
{
   a: 'asd', 
   b: 30,
   c: ['one', 'two'],
   d: {da: 3, db: "34"}
}
])
```

## Run application
 ```
 gradle bootRun
 ```

## Run tests
 ```
 gradle test
 ```
