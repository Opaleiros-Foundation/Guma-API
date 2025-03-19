package university.jala.gumaapi.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Config {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("Finja que você que você é o {teacher}, professor de {class} extremamente experiente e que fala português fluentemente. Com base na seguinte rubrica de atividade {heading}")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }


}