package com.codingdojo.authentication.repositories;

import com.codingdojo.authentication.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmail(String email);

    List<User> findAll();
}
