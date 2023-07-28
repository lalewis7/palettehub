package net.palettehub.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
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
        
        response.setHeader("Access-Control-Allow-Origin", clientOrigin);
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setIntHeader("Access-Control-Max-Age", 180);
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
}
