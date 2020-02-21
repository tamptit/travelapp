package com.travel.config;

import com.travel.entity.User;
import com.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new UsernameNotFoundException("User not found with username : " + email));
        return create(user);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

        return create(user);
    }

    private UserDetails create(User user) {
//    String roleName = user.getRole().getRoleName();
//    List<GrantedAuthority> authorities = new ArrayList<>();
//    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));

        return new UserPrincipal(
                user.getId(), user.getEmail(), user.getPassword(), null, null);
    }
}
