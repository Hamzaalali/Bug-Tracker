package com.example.bugtracker.auth.services;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.acm_backend.auth.entities.*;
import com.example.acm_backend.auth.exceptions.*;
import com.example.acm_backend.auth.requests.RegisterRequest;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final RoleRepo roleRepo;
    private final UserTokensRepo userTokensRepo;

    public User registerUser(RegisterRequest registerRequest) throws EmailAlreadyExistsException, InvalidRoleIdException {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUserName(registerRequest.getUserName());
        Argon2 argon2 = Argon2Factory.create();
        String hash = argon2.hash(10, 65536, 1, registerRequest.getPassword().toCharArray());
        user.setHash(hash);
        List<Role> roles = new ArrayList<>();
        for (Role role : registerRequest.getRoles()) {
            Role dbRole = roleRepo.findRoleById(role.getId());
            if (dbRole == null) {
                throw new InvalidRoleIdException();
            }
            roles.add(dbRole);
        }
        user.setRoles(roles);
        return userService.createUser(user);
    }

    public Map<String, String> login(String email, String password) throws Exception {

        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException();
        }
        Argon2 argon2 = Argon2Factory.create();
        if (!argon2.verify(user.getHash(), password.toCharArray())) {
            throw new InvalidPasswordException();
        }
        String accessToken = generateJwtToken(user, (long) (1000 * 60 * 1), "access_token_secret");
        String refreshToken = generateJwtToken(user, (long) (60 * 60 * 1000 * 24 * 14), "refresh_token_secret");
        UserTokens userTokens = userTokensRepo.findUserTokensByUser(user);
        userTokens.setAccessToken(accessToken);
        userTokens.setRefreshToken(refreshToken);
        userTokensRepo.save(userTokens);
        Map<String, String> accessAndRefreshObject = new HashMap<>();
        accessAndRefreshObject.put("accessToken", accessToken);
        accessAndRefreshObject.put("refreshToken", refreshToken);
        return accessAndRefreshObject;
    }

    public Map<String, String> refresh(String refreshToken) throws InvalidRefreshTokenException, UnauthorizedAccessException {
        try {
            DecodedJWT decodedJWT = validToken(refreshToken, "refresh_token_secret");
            User user = userService.findUserById(decodedJWT.getClaim("id").asLong());
            UserTokens userTokens = userTokensRepo.findUserTokensByUser(user);
            try {
                if (!Objects.equals(refreshToken, userTokens.getRefreshToken())) {
                    throw new UnauthorizedAccessException();
                }
                validToken(userTokens.getAccessToken(), "access_token_secret");
                throw new UnauthorizedAccessException();
            } catch (UnauthorizedAccessException e) {
                deleteTokens(user);
                throw new UnauthorizedAccessException();
            } catch (Exception e) {
                //ignore
            }
            String accessToken = generateJwtToken(user, (long) (1000 * 60 * 1), "access_token_secret");
            Map<String, String> accessTokenObject = new HashMap<>();
            accessTokenObject.put("accessToken", accessToken);
            return accessTokenObject;
        } catch (UnauthorizedAccessException e) {
            throw new UnauthorizedAccessException();
        } catch (Exception e) {
            throw new InvalidRefreshTokenException();
        }
    }

    private String generateJwtToken(User user, Long timeInMilliSecond, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String token = JWT.create()
                .withClaim("email", user.getEmail())
                .withClaim("id", user.getId())
                .withClaim("userName", user.getUserName())
                .withExpiresAt(new Date(System.currentTimeMillis() + timeInMilliSecond))
                .withClaim("roles", user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                .sign(algorithm);
        return token;
    }

    public DecodedJWT validToken(String token, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }
    public User validToken(String token) {
        DecodedJWT decodedJWT =   validToken(token,"access_token_secret");
        Long id = decodedJWT.getClaim("id").asLong();
        User user = userService.findUserById(id);
        return user;
    }
    public void logout(User user) throws FailedToLogoutException {
        try {
            deleteTokens(user);
        } catch (Exception e) {
            throw new FailedToLogoutException();
        }
    }
    public void authorizeRoles(List<Role> userRoles, List<String>apiRoles)  {
        for(Role role:userRoles){
            if(apiRoles.contains(role.getRoleName())){
                return;
            }
        }
        throw new UnauthorizedAccessException();
    }
    private void deleteTokens(User user) {
        UserTokens userTokens = userTokensRepo.findUserTokensByUser(user);
        userTokens.setRefreshToken("");
        userTokens.setAccessToken("");
        userTokensRepo.save(userTokens);
    }
}
