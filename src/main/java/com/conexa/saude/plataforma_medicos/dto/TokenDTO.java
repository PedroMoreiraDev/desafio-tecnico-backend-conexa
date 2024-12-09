package com.conexa.saude.plataforma_medicos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDTO {

    @JsonProperty("token")
    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }

}
