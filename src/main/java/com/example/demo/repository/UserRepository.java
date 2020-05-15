package com.example.demo.repository;

import com.example.demo.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Person, String> {

}
