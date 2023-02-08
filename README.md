# Teda Framework - Data-Driven Testing Framework based on Excel, OpenOffice, etc...

![Maven Workflow](https://github.com/brielmayer/teda/actions/workflows/maven.yml/badge.svg)

Data-Driven Testing Framework.

## Supported Databases

mySQL, PostgresSQL, MS SQL Server, Oracle, MariaDB, H2.

## How to start

1. Implement 'com.brielmayer.teda.handler.ExecutionHandler' Interface.

2. Add your test data with an excel sheet. See our example for more
   details: [Example Excel Sheet](/src/test/resources/teda/LOAD_TEST.xlsx)

3. Now you can implement your tests with your own DataSource. See
   our [example here](/src/test/java/com/brielmayer/teda/suite/PostgresSuiteTest.java)
