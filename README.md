# money-loan

[![Build Status](https://travis-ci.org/dim42/money-loan.svg?branch=master)](https://travis-ci.org/dim42/money-loan)

Simple Java REST service emulating applying for a money loan.

Technologies: Spring Boot, H2 (Embedded), Hibernate 5.2.6, JUnit, Spock, Gradle 3.3.

The unit tests can be run with the Gradle wrapper command-line:

./gradlew test   (on Linux)

gradlew test   (on Windows)

Start the server:

./gradlew bootRun


Country service classes have been generated with:

wsimport http://www.webservicex.net/geoipservice.asmx?WSDL -keep
