package api.forohub.forohub.infra.security;

import api.forohub.forohub.domain.user.User;
import api.forohub.forohub.domain.user.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user object for whom the token is generated.
     * @return JWT token as a String.
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(user.getPassword()); // Use user's password hash for signing
            return JWT.create()
                    .withIssuer("forohub")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId())
                    .withExpiresAt(generateDateTimeExpired())
                    .sign(algorithm); // Sign the token with the algorithm
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error al generar el token", exception);
        }
    }

    /**
     * Retrieves the subject (username) from the JWT token.
     *
     * @param token JWT token string.
     * @return Username (subject) extracted from the token.
     */
    public String getSubject(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token is null");
        }

        try {
            // Decode the JWT token
            DecodedJWT decodedJWT = JWT.decode(token);
            String username = decodedJWT.getSubject(); // Extract username from the token
            if (username == null) {
                throw new IllegalArgumentException("Invalid token: Subject not found");
            }

            // Return the username
            return username;
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid token: " + e.getMessage(), e);
        }
    }

    /**
     * Calculates the expiration date for JWT token (1 day from now).
     *
     * @return Instant representing the expiration date.
     */
    private Instant generateDateTimeExpired() {
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-05:00"));
    }
}
