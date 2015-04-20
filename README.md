![Travis ci image](https://travis-ci.org/guillaumebreton/gocardless-scala.svg?branch=develop)
[![Codacy Badge](https://www.codacy.com/project/badge/cd61c33c70de47f1b707655e774ad8b3](https://www.codacy.com/app/breton-gy/gocardless-scala)
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
