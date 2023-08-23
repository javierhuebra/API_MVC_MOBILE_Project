package com.codingdojo.authentication.controllers;

import com.codingdojo.authentication.clases.Provincias;
import com.codingdojo.authentication.models.Evento;
import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.services.EventoService;
import com.codingdojo.authentication.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventoController {

    private final EventoService eventoService;
    private final UserService userService;

    public EventoController(EventoService eventoService, UserService userService){
        this.eventoService = eventoService;
        this.userService = userService;
    }

    //REGISTRAR UN EVENTO
    @PostMapping("/nuevo/evento")
    public String crearEvento(@Valid @ModelAttribute("evento") Evento evento,
                              BindingResult result, HttpSession sesion,
                              Model viewModel){
        //Validar si la sesion esta activa
        Long userId = (Long) sesion.getAttribute("idLogueado");
        if(userId == null){
            return "redirect:/loginRegister";
        }
        else if(result.hasErrors()){
            User usuario = userService.findUserById(userId);
            viewModel.addAttribute("usuario", usuario);
            viewModel.addAttribute("provincias", Provincias.provincias);
            return "home";
        }
        System.out.println(evento.getFecha());

        evento.setOrganizador(userService.findUserById(userId));

        eventoService.crearEvento(evento);
        return "redirect:/home";

    }

}
