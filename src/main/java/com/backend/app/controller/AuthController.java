package com.backend.app.controller;

import com.backend.app.dto.AuthResponse;
import com.backend.app.dto.LoginRequest;
import com.backend.app.model.Usuario;
import com.backend.app.repository.UsuarioRepository;
import com.backend.app.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        System.out.println("\n>>> Intento de login con: " + request.getEmail());

        Usuario usuario = usuarioRepository.findByEmail(
                request.getEmail().trim().toLowerCase()
        ).orElse(null);

        // Si el usuario NO existe
        if (usuario == null) {
            System.out.println(">>> Usuario NO encontrado");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", true, "message", "Credenciales incorrectas"));
        }

        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), usuario.getPassword());

        // Si la contraseña NO coincide
        if (!passwordMatch) {
            System.out.println(">>> Contraseña incorrecta");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", true, "message", "Credenciales incorrectas"));
        }

        // Si todo es correcto -> generar token
        var authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());

        User userDetails = new User(
                usuario.getEmail(),
                usuario.getPassword(),
                List.of(authority)
        );

        String token = jwtUtil.generateToken(userDetails);

        System.out.println(">>> Login exitoso, token generado");

        return ResponseEntity.ok(
                new AuthResponse(token, usuario.getRol().name())
        );
    }
}
