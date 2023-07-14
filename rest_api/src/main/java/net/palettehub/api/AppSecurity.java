package net.palettehub.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import net.palettehub.api.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class AppSecurity {
    
    @Autowired
	private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((requests) -> requests
            .requestMatchers(HttpMethod.OPTIONS).permitAll()
            .requestMatchers("/auth").permitAll()
            .requestMatchers("/users/**", "/palettes/{paletteId}/like").authenticated()
            .requestMatchers(HttpMethod.GET, "/palettes", "/palettes/{paletteId}").permitAll()
            .requestMatchers(HttpMethod.POST, "/palettes").authenticated()
            .anyRequest().denyAll()
        );
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
