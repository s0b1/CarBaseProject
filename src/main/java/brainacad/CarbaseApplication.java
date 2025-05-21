package brainacad;

import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CarbaseApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CarbaseApplication.class, args);

    }

    @Bean
    public SpringSecurityDialect springSecurityDialect()
    {
        return new SpringSecurityDialect();
    }


}