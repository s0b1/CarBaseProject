package brainacad.service;

import brainacad.model.AppUser;
import brainacad.model.Role;
import brainacad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser register(String username, String rawPassword)
    {
        if (userExists(username))
        {
            throw new IllegalArgumentException("Username already taken");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name().replace("ROLE_", ""))
                .build();
    }

    public boolean userExists(String username)
    {
        return userRepository.findByUsername(username).isPresent();
    }
}




