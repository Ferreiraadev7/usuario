package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final UsuarioConverter usuarioConverter;


  public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
      Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
      usuario = usuarioRepository.save(usuario);
      return usuarioConverter.paraUsuarioDTO(usuario);
  }

}
