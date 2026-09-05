package com.suganya.fhirvitals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication bundles together three annotations:
// - marks this as a configuration class
// - tells Spring to auto-configure itself based on what dependencies
//   are on the classpath (this is where the "magic" people mention
//   about Spring Boot comes from)
// - tells Spring to scan this package (and sub-packages) for
//   @Component/@Service/@RestController/@Repository classes to wire together
@SpringBootApplication
public class FhirVitalsApplication {

    public static void main(String[] args) {
        // This one line starts an embedded web server, wires up every
        // @Service, @RestController, and @Repository we wrote, and
        // starts listening for HTTP requests - equivalent to
        // "if __name__ == '__main__':" plus a whole web framework's
        // startup logic in Python.
        SpringApplication.run(FhirVitalsApplication.class, args);
    }
}