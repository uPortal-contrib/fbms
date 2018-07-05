# Form Builder Microservice (FBMS)

The _Form Builder Microservice_ (FBMS) is REST-based "back end" for Form Builder capabilities
designed for use with Apereo uPortal.

## Running This Project

### Running in a Development Environment

Use the Spring Boot Gradle Plugin to execute this project in a local development environment.

```console
$ ./gradlew fbms-webapp:bootRun
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

### Permissions

Typically authenticated users have permission to read any form, as well as create, read, and update
their own responses to forms.  (In the future FBMS may support *updating* responses both by
overwriting an existing response and submitting a new response.)

As far as managing forms themselves, there are three types of access:

  - `createAuthority`
  - `updateAuthority`
  - `deleteAuthority`

The `createAuthority` allows you to create new forms, whereas `updateAuthority` and `deleteAuthority`
apply to any form that has been previously created.

The default value for each of these authorities is `Portal Administrators`, which corresponds to the
group of the same name in uPortal.

#### Permissions Example

Use the following properties to assign these permissions to one or more different portal groups:

```
org.apereo.portal.fbms.security.createAuthority=Portal Administrators, Forms Authors
org.apereo.portal.fbms.security.updateAuthority=Portal Administrators, Forms Editors
org.apereo.portal.fbms.security.deleteAuthority=Portal Administrators
```

## Import/Export Features

Apereo FBMS provides support for importing and exporting data from the console.  These features are
tremendously useful for provisioning new environments and migrating data.

### Importing

Use the following command to import all Form objects serialized into JSON files that are located in
the `docs/examples` directory:

```bash
$ ./gradlew build fbms-webapp:bootRunImport
```

**NOTE:** The location of this directory is very likely to change or (even more likely) become a
parameter in the future.  Check this document for changes.

## API Documentation

FBMS provides API documentation based on [Swagger][].  You can access the Swagger client at
`http[s]://hostname[:port]/swagger-ui.html`.

[Swagger]: https://swagger.io/