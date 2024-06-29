package api.forohub.forohub.infra.security;

import api.forohub.forohub.domain.user.User;
import api.forohub.forohub.domain.user.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Filters incoming requests to authenticate using JWT token.
     *
     * @param request     HTTP request object.
     * @param response    HTTP response object.
     * @param filterChain Filter chain for additional filters.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Extract Authorization header containing JWT token
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.replace("Bearer ", "");
                // Get subject (username) from token
                String subject = tokenService.getSubject(token);

                if (subject != null) {
                    // Retrieve user from UserRepository based on username
                    User user = (User) userRepository.findByUsername(subject);
                    if (user != null) {
                        // Create Authentication object and set it in SecurityContextHolder
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        throw new IllegalArgumentException("User not found for username: " + subject);
                    }
                }
            }
        } catch (IllegalArgumentException | JWTVerificationException e) {
            // Handle specific exceptions or log errors
            logger.error("Authentication error: " + e.getMessage(), e);
        }
        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}
