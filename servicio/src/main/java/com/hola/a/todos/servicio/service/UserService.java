package com.hola.a.todos.servicio.service;


import com.hola.a.todos.servicio.models.LoginDto;
import com.hola.a.todos.servicio.models.TokenDto;
import com.hola.a.todos.servicio.models.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    public TokenDto login(LoginDto loginDto) {
        var auth = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        try{
            UserDetailsDto user = (UserDetailsDto) authenticationManager.authenticate(auth).getPrincipal();
            String jwt = jwtService.getToken(user);
            return new TokenDto(jwt);
        }catch (Exception ignore){
            throw new RuntimeException("Username o password incorrectos");
        }
    }

}
