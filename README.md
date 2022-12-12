# Teda - Framework by [Brielmayer](https://www.brielmayer.com/)

Data-Driven Testing Framework.

## Supported Databases

mySQL, PostgresSQL, MS SQL Server, Oracle.

## How to start

1. Implement 'com.brielmayer.teda.handler.ExecutionHandler' Interface.

2. Setup your Database and provide connection details within your /test/resources/teda.properties like following:

```
jdbc.input.url=jdbc:postgresql://localhost:5432/database
jdbc.input.user=postgres
jdbc.input.password=mysecretpassword
jdbc.output.url=jdbc:postgresql://localhost:5432/database
jdbc.output.user=postgres
jdbc.output.password=mysecretpassword
```

3. Add your test data with an excel sheet. See our example for more
   details: [Example Excel Sheet](/src/test/resources/teda/LOAD_TEST.xlsx)

4. Now you can implement your tests our [example here](/src/test/java/com/brielmayer/teda/suite/PostgresSuiteTest.java)
