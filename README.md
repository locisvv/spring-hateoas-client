# spring-hateoas-client
The simple rest client with spring hateoas framework.

For more information see:
- [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)
- [Spring-hateoas documentacion](http://docs.spring.io/spring-hateoas/docs/current/reference/html/)


The mapping of the URI path space is presented in the following table:

URI path                                            | Resource class                                | HTTP methods
-------------------------------------               | ----------------                              | --------------
http://localhost:8080/rest/artist/1                 | org.springframework.hateoas.Resource<Artist>; | GET
http://localhost:8080/rest/albums/1                 | com.svv.jaxrs.HalResource\<Album,Artist\>       | GET
http://localhost:8080/rest/albums/1?embedded=true   | com.svv.jaxrs.HalResource\<Album,Artist\>       | GET
http://localhost:8080/rest/albums                   | List\<HalResource\<Album,Artist\>\>               | GET
Running the Example
-------------------
The first you should setup and run the server application which you can download from here: <https://github.com/locisvv/spring-hateoas-example>.

And after run the example as follows:

>     mvn test

or just run main class.
