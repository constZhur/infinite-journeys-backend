package ru.mirea.infinitejourneysbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "infinite-journeys-api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(
        info = @Info(
                title = "Infinite Journeys API",
                version = "1.0",
                description = "API туристического агентства Infinite Journeys"
        )
)
public class InfiniteJourneysBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfiniteJourneysBackendApplication.class, args);
    }

}
