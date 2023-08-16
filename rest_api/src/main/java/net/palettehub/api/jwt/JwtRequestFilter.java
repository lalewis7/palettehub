package net.palettehub.api.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Middleware to extract the JWT from the header of each request and add the user to the security context.
 * <p>
 * The JWT can be found as a bearer token under the "Authorization" header. Once the JWT is extracted from the 
 * header it is validated and the payload (user id) is used for the UsernamePasswordAuthenticationToken. 
 * The security context's currently authenticated principal is then set to this token.
 * <p>
 * Inspiration taken from this <a href="https://www.techgeeknext.com/spring/spring-boot-security-token-authentication-jwt">
 * Article</a>.
 * 
 * @author Arthur Lewis
 * @see UsernamePasswordAuthenticationToken
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // get authorization header
        final String requestTokenHeader = request.getHeader("Authorization");

        // token info
        String token = null;

        // check that jwt starts with "bearer"
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			token = requestTokenHeader.substring(7);
		} else {
			System.out.println("JWT Token does not begin with Bearer String");
            filterChain.doFilter(request, response);
            return;
		}

        // validate token
        if (!jwtUtil.validateToken(token)){
            filterChain.doFilter(request, response);
            return;
        }

        // get user info from token
        String userId = jwtUtil.getUserIdFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);

        // validate token
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // UserDetails userDetails = new User(userId, null, new ArrayList<>());

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userId, null, jwtUtil.getAuthorities(role));
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // After setting the Authentication in the context, we specify
            // that the current user is authenticated. So it passes the
            // Spring Security Configurations successfully.
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		filterChain.doFilter(request, response);
    }
}
