# Form Builder Microservice (FBMS)

The _Form Builder Microservice_ (FBMS) project is an Apereo uPortal Ecosystem component that adds
Form Builder capabilities to uPortal.  This Git repo contains the sources for the _back-end_
elements of this solution;  to use FBMS in uPortal, you must also include its _front-end_ component:
`form-builder`.  The `form-builder` web component is developed independently at the following
location:  [uPortal-contrib/form-builder][].  Front-end, back-end, and uPortal communicate
exclusively through REST APIs.

FBMS is developed with the following Java Platform technologies:

  - Spring Boot
  - Spring Security
  - spring-data-jpa
  - Hibernate
  - Jackson (JSON)

## Running This Project

### Using FBMS in uPortal

FBMS does not need uPortal to run, but can be integrated with uPortal version 5.1 or higher.

If you are not setting up a released version of FBMS, run the following to create a snapshot WAR file for FBMS:

```shell
./gradlew clean build install
```
Note the version for this snapshot that can be found in `gradle.properties`.
 
The remaining steps are ALL completed in your uPortal-start repo.


#### Step One:  Bundling FBMS

In uPortal-start, add an `overlays/fbms/build.gradle` file with the following contents:

```groovy
import org.apereo.portal.start.gradle.plugins.GradleImportExportPlugin

apply plugin: GradleImportExportPlugin

dependencies {
    runtime "org.jasig.portal.fbms:fbms-webapp:${fbmsVersion}@war"
    compile configurations.jdbc
}

war {
    archiveName 'fbms.war'
}

/*
 * Import/Export Support
 */

import org.apereo.portal.start.shell.PortalShellInvoker

dependencies {
    impexp configurations.jdbc
    impexp 'org.springframework.boot:spring-boot-starter-tomcat:2.0.3.RELEASE' // Version should match FBMS
}

dataInit {
    /*
     * Drop (if present) then create the Hibernate-managed schema.
     */
    doLast {
        File serverBase = rootProject.file(rootProject.ext['buildProperties'].getProperty('server.base'))
        File deployDir = new File (serverBase, "webapps/${project.name}")

        ant.setLifecycleLogLevel('INFO')
        ant.java(fork: true, failonerror: true, dir: rootProject.projectDir, classname: 'org.apereo.portal.fbms.ApereoFbmsApplication') {
            classpath {
                pathelement(location: "${deployDir}/WEB-INF/classes")
                pathelement(location: "${deployDir}/WEB-INF/lib/*")
                project.configurations.impexp.files.each {
                    pathelement(location: it.absolutePath)
                }
            }
            sysproperty(key: 'portal.home', value: project.rootProject.ext['buildProperties'].getProperty('portal.home'))
            arg(value: '--init')
            arg(value: '--spring.jpa.hibernate.ddl-auto=create')
        }
    }
    /*
     * Import database entities located anywhere within the folder
     * specified by 'implementation.entities.location'.
     */
    doLast {
        File serverBase = rootProject.file(rootProject.ext['buildProperties'].getProperty('server.base'))
        File deployDir = new File (serverBase, "webapps/${project.name}")
        String implementationEntitiesLocation = PortalShellInvoker.createGroovySafePath(rootProject.ext['buildProperties'].getProperty('implementation.entities.location'))

        ant.setLifecycleLogLevel('INFO')
        ant.java(fork: true, failonerror: true, dir: rootProject.projectDir, classname: 'org.apereo.portal.fbms.ApereoFbmsApplication') {
            classpath {
                pathelement(location: "${deployDir}/WEB-INF/classes")
                pathelement(location: "${deployDir}/WEB-INF/lib/*")
                project.configurations.impexp.files.each {
                    pathelement(location: it.absolutePath)
                }
            }
            sysproperty(key: 'portal.home', value: project.rootProject.ext['buildProperties'].getProperty('portal.home'))
            arg(value: '--import')
            arg(value: "${implementationEntitiesLocation}/fbms")
        }
    }
}
```

Add the following to `gradle.properties` file 
```groovy
fbmsVersion=<version>
```
Replace `<version>` with the version (number) of FBMS you want to use.

Add the following to `settings.gradle` file
```groovy
include 'overlays:fbms'
```

#### Step Two:  Bundling `form-builder`

Add the following to `overlays/resource-server/build.gradle` (inside the
`dependencies {}` section):

```
runtime "org.webjars.npm:uportal__form-builder:${formBuilderVersion}@jar"
```

Add the following to `gradle.properties` (under `# Versions of WebJars included with
resource-server`):

```
formBuilderVersion=<version>
```
Replace `<version>` with the version (number) of `form-builder` you want to use.
See https://mvnrepository.com/artifact/org.webjars.npm/uportal__form-builder

#### Step Three:  Initialize FBMS Database Tables

Form definition JSON files should be copied into a new directory under `data/quickstart/` (or your specific dataset)
named `fbms`. For example:
```shell
mkdir data/quickstart/fbms
cp <fbms_repo_dir>/docs/examples/*.json data/quickstart/fbms/
```

Then, run one of the following to initialize the FBMS data.

To initialize _just_ the FBMS tables and data, run:
```shell
./gradlew :overlays:fbms:dataInit
```
Or, if you want to re-initialize all uPortal, portlet and FBMS data:
```shell
./gradlew dataInit
```
#### Step Four:  Publishing a Form with FBMS

Use a SimpleContentPortlet to publish the following HTML markup as a portlet:

```html
<script src="/resource-server/webjars/uportal__form-builder/build/static/js/form-builder.js"></script>
<form-builder
  fbms-base-url="/fbms"
  fbms-form-fname="<form.fname>"
  oidc-url="/uPortal/api/v5-1/userinfo">
</form-builder>
```

Replace `<form.fname>` with the `fname` of your form (in FBMS). The form's `fname` can be found in its definition file.
These were the files added to `data/quickstart/fbms/`.

### Final Step: Provide uPortal's Signature Key to FBMS

Finally, we need to provide the signature key that uPortal uses to sign the user's JWT for authentication.
This is usually found in uPortal-start's `etc/portal/uPortal.properties` or may have been moved to `global.properties`
in the same directory. It may be commented out to use the default, but the default entry should be there:
```properties
org.apereo.portal.soffit.jwt.signatureKey=CHANGEMEBx0myZ/pv/e7+xrdDLYGC1iIzSa6Uw5CPpH0KCCS1deESk3v+b+LYMz1ks57tjFb9vudpSCyRKXO5TeEBc45rfMyGtkRa1zri+hukZIAfgrvCbFixpCBxBusRs+uhXRuLxOe6k77VE+EMM4jVJArtNBgVPyV7iOC05kHNiYIGgs=
```
Again, this is the default. Your uPortal-start may have a different value.

1. Copy this into `etc/portal/fbms.properties`.
2. Copy this file into your $PORTAL_HOME directory, usually in uPortal-start at `./gradle/tomcat/portal/`
3. Restart Tomcat

This Completes the uPortal Setup Instructions

---------------------------

### Running FBMS with `bootRun`

It is sometimes helpful to run FBMS without uPortal for development purposes.  Use the Spring
Boot Gradle Plugin to launch this project from a local clone of FBMS (this repository).

#### Pre-Requisite: Database Setup

Before using FBMS stand-alone, the database will need to be configured and a driver added as a dependency.

1. The drive should be added to `fbms-webapp/build.gradle` at the end of the dependency section. There already exists
a comment and example for HSQL. You may add any other database driver as needed as a `runtime` dependency.

2. `fbms-webapp/src/main/resources/fbms.properties` should be edited to include database connection configuration.
Other values may also require adjusting based on your needs.
#### Starting FBMS Stand-Alone

```console
$ ./gradlew fbms-webapp:bootRun
```
#### Import/Export Features

Apereo FBMS provides support for importing and exporting data from the console.  These features are
tremendously useful for provisioning new environments and migrating data.

##### Initializing the Database Schema

Use the following command to drop (if necessary) and create the Hibernate-managed database tables:

```bash
$ ./gradlew assemble fbms-webapp:bootRunDataInit
```

##### Importing

Use the following command to import all Form objects serialized into JSON files that are located in
the `docs/examples` directory:

```bash
$ ./gradlew assemble fbms-webapp:bootRunDataImport
```

**NOTE:** The location of this directory is very likely to change or (even more likely) become a
parameter in the future.  Check this document for changes.

## Configuration

:note: FBMS supports the standard uPortal convention for external configuration based on
`$PORTAL_HOME`.  Use `global.properties` for settings that are shared between modules.  Use
`fbms.properties` for settings that are exclusive to this module, or to override a setting defined
in `global.properties`.

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

## API Documentation

FBMS provides API documentation based on [Swagger][].  You can access the Swagger client at
`http[s]://hostname[:port]/swagger-ui.html`.

[uPortal-contrib/form-builder]: https://github.com/uPortal-contrib/form-builder
[Swagger]: https://swagger.io/
