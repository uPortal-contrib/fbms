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

Also add `include 'overlays:fbms'` to your `settings.gradle` file.

### Step Two:  Bundling `form-builder`

In uPortal-start, add the following to `overlays/resource-server/build.gradle` (inside the
`dependencies {}` section):

```
runtime "org.webjars.npm:uportal__form-builder:${formBuilderVersion}@jar"
```

In uPortal-start, add the following to `gradle.properties` (under `# Versions of WebJars included with
resource-server`):

```
formBuilderVersion=<version>
```

Replace `<version>` with the version (number) of `form-builder` you want to use.

### Step Three:  Publishing a Form with FBMS

Use a SimpleContentPortlet to publish the following HTML markup as a portlet:

```html
<script src="/fbms/webjars/uportal__form-builder/0.1.2/build/static/js/form-builder.js"></script>
<form-builder
  fbms-base-url="/fbms"
  fbms-form-fname="<form.fname>"
  oidc-url="/uPortal/api/v5-1/userinfo">
</form-builder>
```

Replace `<form.fname>` with the `fname` of your form (in FBMS).

### Running FBMS with `bootRun`

It is sometimes helpful to run FBMS without uPortal for development purposes.  Use the Spring
Boot Gradle Plugin to launch this project from a local clone of FBMS (this repository).

```console
$ ./gradlew fbms-webapp:bootRun
```

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

## Import/Export Features

Apereo FBMS provides support for importing and exporting data from the console.  These features are
tremendously useful for provisioning new environments and migrating data.

### Initializing the Database Schema

Use the following command to drop (if necessary) and create the Hibernate-managed database tables:

```bash
$ ./gradlew assemble fbms-webapp:bootRunDataInit
```

### Importing

Use the following command to import all Form objects serialized into JSON files that are located in
the `docs/examples` directory:

```bash
$ ./gradlew assemble fbms-webapp:bootRunDataImport
```

**NOTE:** The location of this directory is very likely to change or (even more likely) become a
parameter in the future.  Check this document for changes.

## API Documentation

FBMS provides API documentation based on [Swagger][].  You can access the Swagger client at
`http[s]://hostname[:port]/swagger-ui.html`.

[uPortal-contrib/form-builder]: https://github.com/uPortal-contrib/form-builder
[Swagger]: https://swagger.io/
