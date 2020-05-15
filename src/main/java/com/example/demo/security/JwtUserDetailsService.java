package com.example.demo.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.util.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String token) {
        DecodedJWT decodedJWT = JwtUtil.tokenToJwt(token);

        if (decodedJWT == null) {
            throw new BadCredentialsException("Not valid token");
        }

        String id = decodedJWT.getClaim("id").asString();
        String role = decodedJWT.getClaim("role").asString();

        return new UserDetailsImpl(id, AuthorityUtils.createAuthorityList(role));
    }
}