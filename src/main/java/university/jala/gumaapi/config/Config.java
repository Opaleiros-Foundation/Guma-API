package university.jala.gumaapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class Config {

    @Value("${cors.allowedOrigins:*}")
    private String allowedOrigins;

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("Finja que você que você é o {teacher}, professor de {class} extremamente experiente e que fala português fluentemente. Com base na seguinte rubrica de atividade {heading}")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("GUMA API")
                        .description("API for reviewing assignments")
                        .version("1.0").contact(new Contact().name("Matheus Victor")
                                .email("matheusvictorhenrique@gmail.com")
                                .url("https://github.com/Opaleiros-Foundation"))
                        .termsOfService("https://github.com/Opaleiros-Foundation")
                        .license(new License().name("License of API")
                                .url("https://opensource.org/license/mit/")));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String[] origins = allowedOrigins.split(",");
                registry.addMapping("/**")
                        .allowedOrigins(origins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }


}