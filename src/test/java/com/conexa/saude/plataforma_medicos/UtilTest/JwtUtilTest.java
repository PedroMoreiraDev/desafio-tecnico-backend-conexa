package com.conexa.saude.plataforma_medicos.UtilTest;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.conexa.saude.plataforma_medicos.exceptions.InternalServerException;
import com.conexa.saude.plataforma_medicos.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "api.security.token.secret=secret-key")
class JwtUtilTest {

    @InjectMocks
    private JwtUtil customJwtUtil;

    @Value("${api.security.token.secret}")
    private String secret;

    @Mock
    private JWTVerifier jwtVerifier;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customJwtUtil.setSecret(secret);
    }

    @Test
    void deveGerarTokenComSucesso() {
        String email = "medico@example.com";

        String token = customJwtUtil.generateToken(email);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void deveLancarExceptionErroAoGerarJwtToken() {
        try (MockedStatic<Algorithm> mockedAlgorithm = Mockito.mockStatic(Algorithm.class)) {
            mockedAlgorithm.when(() -> Algorithm.HMAC256(anyString()))
                    .thenThrow(new JWTCreationException("Erro de criação", new Throwable()));

            InternalServerException exception = assertThrows(InternalServerException.class, () -> {
                customJwtUtil.generateToken("medico@example.com");
            });

            assertEquals("API-PLATAFORMA-MEDICOS-0005", exception.getCode());
            assertEquals("Erro ao gerar JWT Token", exception.getMessage());
        }
    }

    @Test
    void deveValidarTokenComSucesso() {
        JWTVerifier mockVerifier = mock(JWTVerifier.class);
        DecodedJWT mockDecodedJWT = mock(DecodedJWT.class);

        String token = "valid.token.value";

        when(mockVerifier.verify(token)).thenReturn(mockDecodedJWT);
        when(mockDecodedJWT.getExpiresAt()).thenReturn(new Date(System.currentTimeMillis() + 10000));

        JwtUtil jwtUtil = new JwtUtil(mockVerifier);

        boolean result = jwtUtil.validateToken(token);

        assertTrue(result);
    }

    @Test
    void deveGerarExceptionErroAoValidarToken() {
        JWTVerifier mockVerifier = mock(JWTVerifier.class);
        DecodedJWT mockDecodedJWT = mock(DecodedJWT.class);

        String token = "expired.token.value";

        when(mockVerifier.verify(token)).thenReturn(mockDecodedJWT);
        when(mockDecodedJWT.getExpiresAt()).thenReturn(new Date(System.currentTimeMillis() - 10000));

        boolean result = customJwtUtil.validateToken(token);

        assertFalse(result);
    }

    @Test
    void deveRetornarFalseTokenInvalido() {
        String token = "invalid.token.value";

        when(jwtVerifier.verify(token)).thenThrow(new JWTVerificationException("Invalid token"));

        boolean result = customJwtUtil.validateToken(token);

        assertFalse(result);
    }

    @Test
    void deveRecuperarSubjectComSucesso() {
        JWTVerifier mockVerifier = mock(JWTVerifier.class);
        DecodedJWT mockDecodedJWT = mock(DecodedJWT.class);

        String token = "valid.token.value";
        String expectedSubject = "medico@example.com";

        when(mockVerifier.verify(token)).thenReturn(mockDecodedJWT);
        when(mockDecodedJWT.getSubject()).thenReturn(expectedSubject);

        JwtUtil jwtUtil = new JwtUtil(mockVerifier);

        String subject = jwtUtil.getSubject(token);

        assertEquals(expectedSubject, subject);
    }

    @Test
    void deveRetornarExceptionErroAoRecuperarSubjectJwtToken() {
        String token = "invalid.token.value";

        when(jwtVerifier.verify(token)).thenThrow(new JWTVerificationException("Invalid token"));

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            customJwtUtil.getSubject(token);
        });

        assertEquals("API-PLATAFORMA-MEDICOS-0005", exception.getCode());
        assertEquals("Erro ao recuperar subject JWT Token", exception.getMessage());
    }
}
