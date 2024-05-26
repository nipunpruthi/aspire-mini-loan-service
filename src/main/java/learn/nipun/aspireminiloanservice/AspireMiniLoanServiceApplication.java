package learn.nipun.aspireminiloanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class AspireMiniLoanServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(AspireMiniLoanServiceApplication.class, args);
    }

}
