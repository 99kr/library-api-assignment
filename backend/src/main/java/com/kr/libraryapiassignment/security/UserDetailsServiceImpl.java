package com.kr.libraryapiassignment.security;

import com.kr.libraryapiassignment.entity.Permission;
import com.kr.libraryapiassignment.entity.Role;
import com.kr.libraryapiassignment.entity.User;
import com.kr.libraryapiassignment.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByEmailIgnoreCase(email);

        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        User user = optUser.get();

        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }

        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(),
                                   user.getLastName(), authorities);
    }
}
