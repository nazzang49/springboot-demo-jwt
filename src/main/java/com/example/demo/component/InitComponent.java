package com.example.demo.component;

import com.example.demo.domain.Person;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@Component
public class InitComponent implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        Person user = new Person("ssna", passwordEncoder.encode("1234"),"USER");
        userRepository.save(user);

        Person admin = new Person("whahn", passwordEncoder.encode("1234"), "ADMIN");
        userRepository.save(admin);
    }
}