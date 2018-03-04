# EurekaClient

## GET requests:

- /list: lists all the numbers in the database as JSON array
Examples:

$ curl -X GET http://localhost:8080/list
[{"number":"1","insert_date":"2018-03-01 23:46:39"},{"number":"3","insert_date":"2018-03-03 16:17:01"},{"number":"10","insert_date":"2010-03-17 12:21:48"},{"number":"23","insert_date":"2010-03-18 12:21:48"}]

$ curl -X GET http://localhost:8080/list?order=descending
[{"number":"23","insert_date":"2010-03-18 12:21:48"},{"number":"10","insert_date":"2010-03-17 12:21:48"},{"number":"3","insert_date":"2018-03-03 16:17:01"},{"number":"1","insert_date":"2018-03-01 23:46:39"}]

- /number/max: returns the max number from the database as JSON object
Example:

$ curl -X GET http://localhost:8080/number/max
{"number":"23","insert_date":"2010-03-18 12:21:48"}

- /number/min: returns the min number from the database as JSON object
Example:

$ curl -X GET http://localhost:8080/number/min
{"number":"1","insert_date":"2018-03-01 23:46:39"}

- /number/{number}: returns the number with the value {number} from the database as JSON object
Example:

$ curl -X GET http://localhost:8080/number/23
{"number":"23","insert_date":"2010-03-18 12:21:48"}

- /delete/{number}: deletes the number with the value {number} from the database
Examples:

$ curl -X GET http://localhost:8080/delete/55
Could not find the number you are trying to delete!

$ curl -X GET http://localhost:8080/delete/3
Number has been deleted successfully!

## POST requests:

- /insert/{number}: inserts new number with the value {number} to the database
Examples:

$ curl -X POST http://localhost:8080/insert/23
This number already exists!

$ curl -X POST http://localhost:8080/insert/66
Number has been inserted successfully!
