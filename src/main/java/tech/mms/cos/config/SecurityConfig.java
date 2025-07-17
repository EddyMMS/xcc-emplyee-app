package tech.mms.cos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static tech.mms.cos.core.model.AccountRole.VIEWER;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, BasicAuthFilter authFilter) throws Exception {

        http
                //.authorizeHttpRequests(auth -> auth.requestMatchers("/accounts/**").hasRole("ADMIN")) //antMatcher, kann sein dass in Datenbank ROLE_Viewer stehen muss
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/github/access_token").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/employees/**").hasAnyRole(VIEWER.name())
                        .requestMatchers("/api/auth/github/callback").permitAll()
                )
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterAfter(authFilter, BasicAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("OPTIONS", "PUT", "DELETE", "GET", "POST");
    }

}



