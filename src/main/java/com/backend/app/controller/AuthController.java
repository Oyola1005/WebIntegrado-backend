package com.backend.app.controller;

import com.backend.app.dto.AuthResponse;
import com.backend.app.dto.LoginRequest;
import com.backend.app.dto.RegisterRequest;
import com.backend.app.model.Pasajero;
import com.backend.app.model.Rol;
import com.backend.app.model.Usuario;
import com.backend.app.repository.PasajeroRepository;
import com.backend.app.repository.UsuarioRepository;
import com.backend.app.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasajeroRepository pasajeroRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasajeroRepository pasajeroRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.pasajeroRepository = pasajeroRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ============================
    // LOGIN
    // ============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        System.out.println("\n>>> Intento de login con: " + request.getEmail());

        String emailNormalizado = request.getEmail().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElse(null);

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

        // Buscar datos de pasajero para el saludo
        Optional<Pasajero> pasajeroOpt = pasajeroRepository.findByEmail(emailNormalizado);
        String nombreMostrado = pasajeroOpt
                .map(p -> p.getNombres() + " " + p.getApellidos())
                .orElse(emailNormalizado); // fallback por si no hubiera pasajero

        AuthResponse resp = new AuthResponse(
                token,
                usuario.getRol().name(),
                nombreMostrado
        );

        return ResponseEntity.ok(resp);
    }

    // ============================
    // REGISTER (crea CLIENTE)
    // ============================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        String emailNormalizado = request.getEmail().trim().toLowerCase();

        // 1) Validar que el email no exista en USUARIOS
        boolean emailEnUso = usuarioRepository.findByEmail(emailNormalizado).isPresent();
        if (emailEnUso) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", true, "message", "El email ya está registrado"));
        }

        // 2) Validar que el DNI no esté repetido en PASAJEROS
        boolean dniEnUso = pasajeroRepository.findByDni(request.getDni()).isPresent();
        if (dniEnUso) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", true, "message", "El DNI ya está registrado como pasajero"));
        }

        // 3) Crear USUARIO con rol CLIENTE
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(emailNormalizado);
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setRol(Rol.CLIENTE);
        nuevoUsuario.setEnabled(true);
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());

        usuarioRepository.save(nuevoUsuario);

        // 4) Crear PASAJERO asociado a ese email
        Pasajero pasajero = new Pasajero(
                null,
                request.getNombres(),
                request.getApellidos(),
                request.getDni(),
                emailNormalizado,
                request.getTelefono()
        );
        pasajeroRepository.save(pasajero);

        // 5) Generar token y respuesta con nombreMostrado
        var authority = new SimpleGrantedAuthority("ROLE_" + nuevoUsuario.getRol().name());
        User userDetails = new User(
                nuevoUsuario.getEmail(),
                nuevoUsuario.getPassword(),
                List.of(authority)
        );
        String token = jwtUtil.generateToken(userDetails);

        String nombreMostrado = request.getNombres() + " " + request.getApellidos();

        AuthResponse resp = new AuthResponse(
                token,
                nuevoUsuario.getRol().name(),
                nombreMostrado
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
