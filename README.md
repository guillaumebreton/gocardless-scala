![Travis ci image](https://travis-ci.org/guillaumebreton/gocardless-scala.svg?branch=develop) ![Codacy Badge](https://www.codacy.com/project/badge/cd61c33c70de47f1b707655e774ad8b3)
gocardless-scala
================

A scala client library for the GoCardless API using akka-http and spray-json. The client is asynchronous and use 
scalaz to return disjonction between error and result.

TODO
====

- publish scala doc to gh-pages
- publish to bintray/maven central
- Enable implicit execution context in each method
- Create client configuration with "Sandbox" and "production" as constants
- Add a method next() on cursor

