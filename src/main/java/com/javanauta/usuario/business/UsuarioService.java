package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.exception.ConflictExceptions;
import com.javanauta.usuario.infrastructure.exception.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final UsuarioConverter usuarioConverter;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){

      emailExiste(usuarioDTO.getEmail());
      usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
      Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
      usuario = usuarioRepository.save(usuario);
      return usuarioConverter.paraUsuarioDTO(usuario);
  }

    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if (existe){
                throw new ConflictExceptions("Email já cadastrado" +  email);
            }
        }catch (ConflictExceptions e){
            throw new ConflictExceptions("Email já cadstrado" + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado" + email));
    }

    public void deletaUsuarioPorEmail(String email){

      usuarioRepository.deleteByEmail(email);
    }

    //Buscamos o email do usuario atraves do token, tirando a obrigatoriedade de passar o email
    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
      // Buscamos os dados do usuario no banco de dados
        String email = jwtUtil.extractEmailToken(token.substring(7));

        //Criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não encontrado"));

        //Mesclou os dados que recebemos na requisicao dto com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //Salvou os dados do usuario convertido e depois pegou o retorno e converteu para usuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
        }
    }

