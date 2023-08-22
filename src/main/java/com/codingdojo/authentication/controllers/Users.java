package com.codingdojo.authentication.controllers;

import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.services.UserService;
import com.codingdojo.authentication.validator.UserValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller //Esto se llama Users igual que el modelo pero es el Controller je
public class Users {
    private final UserService userService;

    //Para validaciones personalizadas
    private final UserValidator userValidator;



    public  Users(UserService userService, UserValidator userValidator){
        this.userService = userService;
        this.userValidator = userValidator;
    }

    // GET Y POST PARA REGISTRO ----------------------
    @GetMapping("/loginRegistration")
    public String renderRegisterForm(@ModelAttribute("user") User user){
        return "loginRegistrationPage";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result, HttpSession session){ //SI QUIERO USAR MENSAJES FLASH RECORDAR PONER RedirectAttributes redirectAttributes
        userValidator.validate(user,result);
        if(result.hasErrors()){
            System.out.println("hay error arriba");
            return "loginRegistrationPage";
        }else{

        //    if(!user.getPassword().equals(user.getPasswordConfirmation())){ //ESTO ES PARA ENVIAR UN MENSAJE FLASH PARA EL ERROR, ES OTRA FORMITA DE COMUNICAR
        //        redirectAttributes.addFlashAttribute("errorPasswords", "Las contraseñas no coinciden!");
        //        System.out.println("contras no coinciden");
        //        return "redirect:/registration";
        //    }

            userService.registerUser(user);
            session.setAttribute("idLogueado", user.getId());//Guardo el id del usuario en la sesion

            return "redirect:/home";
        }
    }

    // GET Y POST PARA LOGIN -------------------------


    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user,
                            Model model, HttpSession session){
        System.out.println(user.getEmail() + user.getPassword());
        if(userService.authenticateUser(user.getEmail(),user.getPassword())){
            session.setAttribute("idLogueado",userService.findByEmail(user.getEmail()).getId());//Busco el usuario por email y saco el id con el getter

            return "redirect:/home";
        }else{
            System.out.println("hay errores");
            model.addAttribute("errorLogin", "Credenciales inválidas.");
            return "loginRegistrationPage"; //Esto podria ser un flash atribute y andaria mejor
        }
    }

    // GET PARA HOME Y LOGOUT
    @GetMapping("/home")
    public String renderHome(HttpSession session, Model model){
        Long idLogueado = (Long) session.getAttribute("idLogueado"); //Hay que acordarse de castear a Long
        if( idLogueado != null){
            User userLogueado = userService.findUserById(idLogueado);
            model.addAttribute("usuario", userLogueado);
            return "home";
        }else{
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
