package net.palettehub.api;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.palettehub.api.jwt.JwtRequestFilter;

/**
 * Handles the security for the spring boot application.
 */
@Configuration
@EnableWebSecurity
public class AppSecurity {
    
    @Autowired
	private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.formLogin(login -> login.disable());
        http.exceptionHandling(exHandle -> {
            exHandle.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
            exHandle.accessDeniedHandler(new AccessDeniedHandler() {

                @Override
                public void handle(HttpServletRequest request, HttpServletResponse response,
                        AccessDeniedException accessDeniedException) throws IOException, ServletException {
                    
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                        response.setCharacterEncoding("UTF-8");

                        PrintWriter out = response.getWriter();
                        out.write("Access denied.");
                        out.flush();
                        out.close();

                }
                
            });
        });
        http.authorizeHttpRequests((requests) -> requests
            // allow pre flight request for cors
            .requestMatchers(HttpMethod.OPTIONS).permitAll()
            // allow anything to auth
            .requestMatchers("/auth").permitAll()
            // -> /users/{userId}
            .requestMatchers("/users/{userId}").permitAll()
            .requestMatchers(HttpMethod.PUT, "/users/{userId}").authenticated()
            // -> /users/{userId}/likes
            .requestMatchers("/users/{userId}/likes").permitAll()
            // -> /users/{userId}/palettes
            .requestMatchers("/users/{userId}/palettes").permitAll()
            // -> /users/{userId}/collections
            .requestMatchers("/users/{userId}/collections").permitAll()
            // -> /palettes
            .requestMatchers("/palettes").permitAll()
            .requestMatchers(HttpMethod.POST, "/palettes").authenticated()
            // -> /palettes/{paletteId}
            .requestMatchers("/palettes/{paletteId}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/palettes/{paletteId}").authenticated()
            // -> /palttes/{paletteId}/like
            .requestMatchers("/palettes/{paletteId}/like").permitAll()
            .requestMatchers(HttpMethod.POST, "/palettes/{paletteId}/like").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/palettes/{paletteId}/like").authenticated()
            // -> /collections
            .requestMatchers("/collections").permitAll()
            .requestMatchers(HttpMethod.POST, "/collections").authenticated()
            // -> collections/{collectionId}
            .requestMatchers("/collections/{collectionId}").permitAll()
            .requestMatchers(HttpMethod.PUT, "/collections/{collectionId}").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/collections/{collectionId}").authenticated()
            // -> /collections/{collectionId}/palettes/{paletteId}
            .requestMatchers("/collections/{collectionId}/palettes/{paletteId}").permitAll()
            .requestMatchers(HttpMethod.POST, "/collections/{collectionId}/palettes/{paletteId}").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/collections/{collectionId}/palettes/{paletteId}").authenticated()
            // any other request
            .anyRequest().denyAll()
        );
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
