package com.codingdojo.authentication.controllers;

import com.codingdojo.authentication.clases.UserLoginRequest;
import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin //Esto para poder habilitar el origen de cualquier lado
public class ApiAuthentication {

    private final UserService userService;

    public ApiAuthentication(UserService userService){
        this.userService = userService;
    }
    //TRAER TODOS LOS USUARIOS - GET
    @GetMapping("/usuarios")
    public List<String> traerTodosLosUsuarios(){
        return userService.todosLosMailsDeUsuario();
    }

    //REGISTRAR USURARIOS - POST
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){

        if(!user.getPassword().equals(user.getPasswordConfirmation())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);//Jejej devuelvo una respuesta con cuerpo nulo y tirando errorcito
        }
        User savedUser = userService.registerUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    //LOGUEAR USUARIOS - POST
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserLoginRequest userLogin){
        if(userService.authenticateUser(userLogin.getEmail(),userLogin.getPassword())){
          User usuarioLogueado = userService.findByEmail(userLogin.getEmail());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioLogueado);//Aca podria mandar el token para comprobar la sesion y eso en el celular, tengo que ver
        }else{
            String errorMessage = "Las credenciales no son validas.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }
}
