package com.app_language.hoctiengtrung_online.Ticket.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn tuyệt đối của thư mục uploads trong project
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();

        // Map đường dẫn URL /uploads/** vào thư mục vật lý
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}