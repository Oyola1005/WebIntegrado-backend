package com.backend.app.service;

import com.backend.app.model.Usuario;
import com.backend.app.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() ->
                        new UsernameNotFoundException("No existe usuario con email: " + email));

        String roleName = "ROLE_" + usuario.getRol().name(); // ROLE_ADMIN, etc.

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(roleName));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),   // hash BCrypt de la BD
                usuario.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
