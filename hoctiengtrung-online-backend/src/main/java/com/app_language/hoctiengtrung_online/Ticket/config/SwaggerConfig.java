package com.app_language.hoctiengtrung_online.Ticket.config;



import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Ticket Management API",
                version = "1.0",
                description = "API for managing artists, shows, tickets, etc."
        )
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas("ArtistRequest", new Schema()
                                .addProperty("name", new StringSchema().example("Sơn Tùng M-TP"))
                                .addProperty("description", new StringSchema().example("Ca sĩ nhạc Pop"))
                                .addProperty("companyCode", new StringSchema().example("COMP001"))
                                .addProperty("imageUrl", new StringSchema().example("https://example.com/image.jpg"))
                        )
                );
    }
}