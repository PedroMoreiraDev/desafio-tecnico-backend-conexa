package com.conexa.saude.plataforma_medicos.service;

import com.auth0.jwt.interfaces.JWTVerifier;
import com.conexa.saude.plataforma_medicos.dto.LoginDTO;
import com.conexa.saude.plataforma_medicos.dto.MedicoDTO;
import com.conexa.saude.plataforma_medicos.dto.TokenDTO;
import com.conexa.saude.plataforma_medicos.exceptions.ErrorMessage;
import com.conexa.saude.plataforma_medicos.exceptions.ValidationException;
import com.conexa.saude.plataforma_medicos.model.entity.JWTBlocklist;
import com.conexa.saude.plataforma_medicos.model.entity.Medico;
import com.conexa.saude.plataforma_medicos.repository.JWTBlocklistRepository;
import com.conexa.saude.plataforma_medicos.repository.MedicoRepository;
import com.conexa.saude.plataforma_medicos.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JWTVerifier verifier;
    private final JWTBlocklistRepository jwtBlocklistRepository;


    public void register(MedicoDTO medicoDTO) {
        if (medicoRepository.existsByCpf(medicoDTO.getCpf())) {
            throw new ValidationException(ErrorMessage.CPF_MEDICO_JA_CADASTRADO);
        }
        if (medicoRepository.existsByEmail(medicoDTO.getEmail())) {
            throw new ValidationException(ErrorMessage.EMAIL_MEDICO_JA_CADASTRADO);
        }

        Medico medico = Medico.builder()
                .email(medicoDTO.getEmail())
                .senha(passwordEncoder.encode(medicoDTO.getSenha()))
                .especialidade(medicoDTO.getEspecialidade())
                .cpf(medicoDTO.getCpf())
                .dataNascimento(medicoDTO.getDataNascimento())
                .telefone(medicoDTO.getTelefone())
                .build();
        medicoRepository.save(medico);
    }

    public TokenDTO authenticate(LoginDTO loginDTO) {
        Medico medico = medicoRepository.findByEmail(loginDTO.getEmail()).orElseThrow(() -> new ValidationException(ErrorMessage.USUARIO_NAO_LOCALIZADO));

        if (!passwordEncoder.matches(loginDTO.getSenha(), medico.getSenha())) {
            throw new ValidationException(ErrorMessage.SENHA_INCORRETA);
        }

        String token = jwtUtil.generateToken(medico.getEmail());
        return new TokenDTO(token);

    }

    public void adicionarTokenNaBlocklist(String token) {
        JWTBlocklist blocklist = new JWTBlocklist();
        blocklist.setToken(token);
        blocklist.setRevokedAt(LocalDateTime.now());
        jwtBlocklistRepository.save(blocklist);
    }

    public boolean isTokenRevogado(String token) {
        return jwtBlocklistRepository.findByToken(token).isPresent();
    }
}



