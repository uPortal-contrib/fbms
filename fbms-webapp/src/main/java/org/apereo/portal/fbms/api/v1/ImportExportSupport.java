package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apereo.portal.fbms.data.FormRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Implements console-based import capabilities for integration with uPortal-start Import/Export.
 * This bean checks for a command-line argument;  if present, it runs and import and shuts the
 * Spring application down.
 */
@Component
public class ImportExportSupport implements CommandLineRunner {

    /**
     * Token indicating that the process will drop and recreate the Hibernate-managed tables (via
     * <code>--spring.jpa.hibernate.ddl-auto=create</code>) and should exit upon completion.  May be
     * chained with <code>IMPORT_TOKEN</code> (though this one must always come first).
     */
    private static final String INIT_TOKEN = "--init";

    /**
     * Token indicating that succeeding command-line arguments are system paths of resources to
     * import.  May be chained with <code>INIT_TOKEN</code> (which must always come first).
     */
    private static final String IMPORT_TOKEN = "--import";

    private static final String JSON_FILE_EXTENSION = ".json";

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private FormRepository formRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Pass <code>--import {fileOrFolder} [{fileOrFolder}...]</code> to import from the command-line.
     * All remaining arguments after <code>--import</code> are things to import.
     */
    @Override
    public void run(String... args) throws Exception {

        final List<String> list = Arrays.asList(args);
        if (list.size() != 0) {
            logger.info("Received the following command-line arguments:  {}", list);
        }

        // Handle imports...
        if (list.contains(IMPORT_TOKEN)) {
            /*
             * This is a console-based import operation.  We will do our work, then shut the String
             * application down.
             */
            final List<String> paths = list.subList(list.indexOf(IMPORT_TOKEN) +1, list.size());
            paths.forEach(location -> {
                logger.info("Importing data from the specified location (including descendants, if appropriate):  {}", location);
                importSpecifiedLocation(Paths.get(location));
            });
        }

        // Terminate the process on either an init or import (or both)...
        if (list.contains(INIT_TOKEN) || list.contains(IMPORT_TOKEN)) {
            SpringApplication.exit(context);
        }

    }

    private void importSpecifiedLocation(Path location) {
        if (!Files.exists(location)) {
            throw new RuntimeException("The specified location does not exist:  " + location);
        }

        try {
            if (Files.isDirectory(location)) {
                // Folder?  (Recurse)
                Files.newDirectoryStream(location)
                        .forEach(child -> importSpecifiedLocation(child));
            } else {
                // File?  (Consider importing)
                if (location.toString().toLowerCase().endsWith(JSON_FILE_EXTENSION)) {
                    logger.info("Importing JSON file:  {}", location);
                    importJsonFile(location);
                } else {
                    logger.info("IGNORING the file (because it doesn't end with {}):  {}", JSON_FILE_EXTENSION, location);
                }

            }


        } catch (Exception e) {
            throw new RuntimeException("Failed to import the specified location:  " + location, e);
        }
    }

    private void importJsonFile(Path location) throws IOException {
        try (InputStream inpt = Files.newInputStream(location)) {
            final RestV1Form form = mapper.readValue(inpt, RestV1Form.class);
            if (formRepository.existsByFname(form.getFname())) {
                logger.warn("Unable to import the Form at location {};  a Form with fname='{}' already exists",
                        location, form.getFname());
            } else {
                form.setVersion(1);  // New Form must be version 1
                formRepository.save(RestV1Form.toEntity(form));
            }
        } catch (Exception e) {
            logger.warn("Unable to import the Form at location {}", location, e);
        }
    }

}
