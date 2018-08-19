package com.boichenko.phonebook.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.boichenko.phonebook.db.UserRepository;
import com.boichenko.phonebook.exception.NotAuthorizedException;
import com.boichenko.phonebook.exception.TokenVerificationException;
import com.boichenko.phonebook.model.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
public class AuthService {

    private static final String TOKEN_ALIAS = "phonebook";
    private KeyPair keyPair = null;

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String authenticate(String userName, String password) {
        User user = userRepository.getUser(userName);
        boolean authResult = authenticate(user, password);
        if (authResult) {
            return createJWT(user);
        } else {
            throw new NotAuthorizedException("User '" + userName + "' not authorized");
        }
    }

    public User authenticateByToken(String token) {
        DecodedJWT decodedJWT = verifyJWT(token);
        int userId = decodedJWT.getClaim("user_id").as(Integer.class);
        User user = new User();
        user.setId(userId);
        return user;
    }

    public void register(String userName, String password) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(createMD5(password));
        userRepository.registerUser(user);
    }

    private boolean authenticate(User user, String password) {
        String md5PasswordValue = createMD5(password);
        return Objects.equals(md5PasswordValue, user.getPassword());
    }

    private String createJWT(User user) {
        KeyPair keyPair = getKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            return JWT.create()
                    .withIssuer(TOKEN_ALIAS)
                    .withClaim("user", user.getUserName())
                    .withClaim("user_id", user.getId())
                    .withExpiresAt(Date.from(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC)))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("JWT creation error");
        }
    }

    private DecodedJWT verifyJWT(String token) {
        KeyPair keyPair = getKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new TokenVerificationException("Token verification failed");
        }
    }

    private KeyPair getKeyPair() {
        if (keyPair == null) {
            try {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                InputStream inputStream = new ClassPathResource("keystore.jks").getInputStream();
                keyStore.load(inputStream, "password".toCharArray());

                RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey(TOKEN_ALIAS, "qwerty".toCharArray());
                Certificate cert = keyStore.getCertificate(TOKEN_ALIAS);
                RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();
                keyPair = new KeyPair(publicKey, privateKey);
            } catch (KeyStoreException | IOException |
                    CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                throw new RuntimeException("KeyStore load error: " + e.getMessage(), e);
            }
        }
        return keyPair;
    }

    private static String createMD5(String source) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5Value = messageDigest.digest(source.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aMd5Value : md5Value) {
                sb.append(Integer.toString((aMd5Value & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
