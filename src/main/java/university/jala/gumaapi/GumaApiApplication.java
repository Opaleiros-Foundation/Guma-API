package university.jala.gumaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GumaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GumaApiApplication.class, args);
    }

}