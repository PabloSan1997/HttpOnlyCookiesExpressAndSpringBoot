package com.hola.a.todos.servicio.controllers;


import com.hola.a.todos.servicio.models.LoginDto;
import com.hola.a.todos.servicio.service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        var res = userService.login(loginDto);

        ResponseCookie cookie = ResponseCookie.from("mitoken", res.getJwt())
                .httpOnly(true)
                .path("/")
                .secure(false)
                .maxAge(3600)
                .sameSite("Lax")
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(httpHeaders).body(res);
    }

    @GetMapping("/dato")
    public ResponseEntity<?> viewData(){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Hola, si lees esto es que ya sabes entrar...");
        map.put("timestamp", new Date().getTime());
        return ResponseEntity.ok(map);
    }
    @GetMapping("/dato2")
    public ResponseEntity<?> viewDat2(){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Hola, si lees esto es que ya sabes entrar... con cookies");
        map.put("timestamp", new Date().getTime());
        return ResponseEntity.ok(map);
    }
    @PostMapping("/dato2")
    public ResponseEntity<?> viewDat2post(@CookieValue(name = "mitoken") String mitoken){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Hola, si lees esto es que ya sabes entrar... con cookies");
        map.put("timestamp", new Date().getTime());
        map.put("token", mitoken);
        ResponseCookie cookie = ResponseCookie.from("mitoken", "")
                .httpOnly(true)
                .path("/")
                .secure(false)
                .maxAge(0)
                .sameSite("Lax")
                .build();
        return ResponseEntity.status(201).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(map);
    }
}
