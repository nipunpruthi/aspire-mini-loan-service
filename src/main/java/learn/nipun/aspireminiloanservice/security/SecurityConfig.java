package learn.nipun.aspireminiloanservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("user1").password(passwordEncoder().encode("userpass")).authorities("CUSTOMER").build());
        manager.createUser(
                User.withUsername("user2").password(passwordEncoder().encode("userpass")).authorities("CUSTOMER").build());
        manager.createUser(
                User.withUsername("admin1").password(passwordEncoder().encode("adminpass")).authorities("ADMIN").build());
        return manager;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }
}
