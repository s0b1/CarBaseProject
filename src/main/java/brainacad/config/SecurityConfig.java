package brainacad.config;

import brainacad.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{

    private final AppUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/img/**").permitAll()

                        .requestMatchers("/drivers/**", "/vehicles/**", "/routes/**", "/cargo/**", "/stats").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        .requestMatchers("/drivers/add", "/drivers/edit/**", "/drivers/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/vehicles/add", "/vehicles/edit/**", "/vehicles/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/routes/add", "/routes/edit/**", "/routes/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/cargo/add", "/cargo/edit/**", "/cargo/delete/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder)
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}

