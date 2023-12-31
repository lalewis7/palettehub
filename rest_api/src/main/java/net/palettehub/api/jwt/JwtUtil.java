package net.palettehub.api.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Utilities class for JWT functionalities such as retrieving payload, checking validity, and generating tokens. 
 * The JWT secret is accessed from the application.properties under "jwt.secret". Using docker this value is 
 * attached from docker secrets to the container. The value is then accessed from the file using 
 * <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config.files.configtree">
 * Spring Boot Configuration Trees.</a> 
 * <p>
 * This <a href="https://www.javainuse.com/spring/boot-jwt">article</a> was used for inspiration.
 * 
 * @author Arthur Lewis
 */
@Component
public class JwtUtil {

	/**
	 * Name for the role claim in JWT payload.
	 */
	public static final String ROLE_CLAIM = "role";
    
	/**
	 * JWT secret.
	 */
    @Value("${jwt.secret}")
	private String secret;

	/**
	 * Lifespan of tokens in seconds.
	 */
	@Value("${jwt.lifespan}")
	private long lifespan;

    /**
	 * Retrieves the user id from the payload of a token.
	 * @param token JWT token.
	 * @return user id value from payload.
	 */
	public String getUserIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * Retrieves expiration date if token from payload.
	 * @param token JWT token.
	 * @return expiration date from payload.
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public String getRoleFromToken(String token) {
		return getClaimFromToken(token, claims -> {
			return claims.get(ROLE_CLAIM).toString();
		});
	}

	/**
	 * Gets a specific claim from a token using the function.
	 * @param <T> Type of value.
	 * @param token JWT token.
	 * @param claimsResolver Function used on token claims.
	 * @return result of using claimsResolver on claims from token.
	 */
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

    // for retrieveing any information from token we will need the secret key
	/**
	 * Retrieves all the claims from a token.
	 * @param token JWT token.
	 * @return Claims of token.
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * Checks if a token is expired at the moment.
	 * @param token JWT token.
	 * @return True if the token is expired, false otherwise.
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	/**
	 * Generates a new token with a given subject (user id).
	 * These steps are followed while creating the token:<p>
	 * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID.<p>
	 * 2. Sign the JWT using the HS512 algorithm and secret key.<p>
	 * 3. According to <a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1">
	 * JWS Compact Serialization</a> compaction of the JWT to a URL-safe string.
	 * @param subject subject of token (user id).
	 * @return JWT token.
	 */
	public String generateToken(String subject, Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + lifespan * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * Generates a new token with just the subject and no other claims.
	 * @param subject subject for payload.
	 * @return JWT token.
	 */
	public String generateToken(String subject) {
		Map<String, Object> claims = new HashMap<>();
		return generateToken(subject, claims);
	}

	/**
	 * Checks the validity of a token using the jwt secret.
	 * @param token JWT token.
	 * @return True if the JWT is valid, false otherwise.
	 */
	public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (IllegalArgumentException e) {
			System.out.println("Unable to get JWT Token");
		} catch (ExpiredJwtException e) {
			System.out.println("JWT Token has expired");
		} catch (MalformedJwtException ex) {
			System.out.println("JWT is invalid");
		} catch (UnsupportedJwtException ex) {
			System.out.println("JWT is not supported");
		} catch (SignatureException ex) {
			System.out.println("Signature validation failed");
		}
        return false;
    }

	public Map<String, Object> getClaims(String role){
		Map<String, Object> claims = new HashMap<>();
		claims.put(ROLE_CLAIM, role);
		return claims;
	}

	public Collection<? extends GrantedAuthority> getAuthorities(String role) {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

        return list;
    }

}
