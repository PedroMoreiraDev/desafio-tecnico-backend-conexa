package com.conexa.saude.plataforma_medicos.repository;

import com.conexa.saude.plataforma_medicos.model.entity.JWTBlocklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JWTBlocklistRepository extends JpaRepository<JWTBlocklist, Long> {

    Optional<JWTBlocklist> findByToken(String token);
}
