package mx.aragon.unam.service.usuario;

import lombok.RequiredArgsConstructor;
import mx.aragon.unam.model.entity.usuario.UsuarioEntity;
import mx.aragon.unam.repository.usuario.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UsuarioDetailsServiceImpl implements UserDetailsService {
//    private final UsuarioRepository usuarioRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return usuarioRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
//    }
//}
