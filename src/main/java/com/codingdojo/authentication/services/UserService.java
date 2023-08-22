package com.codingdojo.authentication.services;

import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //Para traer todos los usuarios que hay registrados
    public List<String> todosLosMailsDeUsuario(){
        List<String> mailsUsuarios = new ArrayList<>();
        for(User usuario : userRepository.findAll()){

            mailsUsuarios.add(usuario.getEmail());
        }
        return mailsUsuarios;
    }

    // Para registrar el usuario y hashear la password
    public User registerUser(User user){
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        return userRepository.save(user);
    }

    // Encontrar un usuario por su email
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    // Encontrar usuario por ID
    public User findUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    // Autenticar usuario
    public boolean authenticateUser(String email, String password){
        User user = userRepository.findByEmail(email);

        if(user == null){
            return false;
        }else{
            return BCrypt.checkpw(password, user.getPassword());//Se simplificó el if porque devuelve un booleano directamente si esta true sino false
        }
    }
}
