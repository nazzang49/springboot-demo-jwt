package com.example.demo.filter;

import com.example.demo.domain.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public AjaxAuthenticationFilter(RequestMatcher requestMatcher, ObjectMapper objectMapper) {
        super(requestMatcher);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isJson(request)) {
            Person person = objectMapper.readValue(request.getReader(), Person.class);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(person.getId(), person.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        } else {
            throw new AccessDeniedException("Not JSON type");
        }
    }

    private boolean isJson(HttpServletRequest request) {
        return MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType());
    }
}
