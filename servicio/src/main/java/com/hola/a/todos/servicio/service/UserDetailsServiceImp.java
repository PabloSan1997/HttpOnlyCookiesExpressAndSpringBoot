package com.hola.a.todos.servicio.service;

import com.hola.a.todos.servicio.models.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsDto user = new UserDetailsDto();
        user.setUsername(username);
        user.setAuthorityAsString("ROLE_USER");
        user.setPassword(passwordEncoder.encode("hola123"));
        return user;
    }
}
