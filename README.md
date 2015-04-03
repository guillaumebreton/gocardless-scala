![Travis ci image](https://travis-ci.org/guillaumebreton/gocardless-scala.svg?branch=develop)

gocardless-scala
================

The scala client library for the GoCardless API

Idea
====


```scala
import gocardless._

implicit client = Gocardless()
Customer.all()
Customer.create("surname" -> "test" ):Future[Customer]
Customer.update("surname" -> "test1" ):Future[Customer]
client.close()
```
