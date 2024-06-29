package api.forohub.forohub.infra.security;

import api.forohub.forohub.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario en UserRepository
        UserDetails userDetails = userRepository.findByUsername(username);

        // Si el usuario no existe, lanzar una excepción UsernameNotFoundException
        if (userDetails == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username);
        }

        return userDetails;
    }
}
