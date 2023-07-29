package net.palettehub.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles the cors headers on responses.
 * 
 * @author Arthur Lewis
 */
@Component
public class CorsFilter implements Filter{

    @Value("${client.web.origin}")
	private String clientOrigin;

    private final String[] whitelistedOrigins = {
        "http://localhost:3000" // react development origin
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /*
         * "The value "*" only counts as a special wildcard value for requests without credentials 
         * (requests without HTTP cookies or HTTP authentication information). In requests with 
         * credentials, it is treated as the literal header name "*" without special semantics. 
         * Note that the Authorization header can't be wildcarded and always needs to be 
         * listed explicitly."
         * 
         * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Allow-Headers
         */

        // allow multiple origins
        String requestOrigin = request.getHeader("Origin");
        if (isAllowedOrigin(requestOrigin, Stream.concat(Arrays.stream(whitelistedOrigins), 
                Arrays.stream(new String[]{clientOrigin})).toArray(String[]::new))){
            response.setHeader("Access-Control-Allow-Origin", requestOrigin);
        }

        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setIntHeader("Access-Control-Max-Age", 180);
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isAllowedOrigin(String origin, String[] allowedOrigins){
        for (String allowedOrigin : allowedOrigins) {
            if(origin.equals(allowedOrigin)) return true;
        }
        return false;
    }
    
}
