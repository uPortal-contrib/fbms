# Form Builder Microservice (FBMS)

The _Form Builder Microservice_ (FBMS) is REST-based "back end" for Form Builder capabilities
designed for use with Apereo uPortal.

## Running This Project

### Running in a Development Environment

Use the Spring Boot Gradle Plugin to execute this project in a local development environment.

```console
$ ./gradlew bootRun
```

## Configuration

### CORS Support

Browser-based clients for this microservice will often need to run on a different host or port.  In
these cases, support for CORS is required.

Use the `org.apereo.portal.fbms.api.cors.origins` application property to specify allowed origins
for CORS requests.  The default value of this property is `http://localhost:8080`.

#### CORS Example

```
org.apereo.portal.fbms.api.cors.origins=http://localhost:8080
```

## API Documentation

FBMS provides API documentation based on [Swagger][].  You can access the Swagger client at
`http[s]://hostname[:port]/swagger-ui.html`.

[Swagger]: https://swagger.io/