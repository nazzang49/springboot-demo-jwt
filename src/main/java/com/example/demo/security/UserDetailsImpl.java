package com.example.demo.security;

import com.example.demo.domain.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

public class UserDetailsImpl extends User {

    public UserDetailsImpl(String id, List<GrantedAuthority> authorities) {
        super(id, "", authorities);
    }

    public UserDetailsImpl(Person person, List<GrantedAuthority> authorities) {
        super(person.getId(), person.getPassword(), authorities);
    }
}