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
import org.springframework.web.bind.annotation.*;

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

    //EDITAR EVENTO - GET
    @GetMapping("/events/{idEvento}/edit")
    public String viewEdicionEvento(@PathVariable("idEvento") Long idEvento,
                                    @ModelAttribute("evento") Evento evento,
                                    HttpSession sesion,
                                    Model viewModel){
        Long userId = (Long) sesion.getAttribute("idLogueado");
        if(userId == null){
            return "redirect:/loginRegister";
        }
        Evento eventoEditar = eventoService.buscarEvento(idEvento); //Busco el evento para enviarlo en el modelo y que se muestre en los inputs!
        if (eventoEditar == null){
            return "redirect:/events";
        }
        User usuario = userService.findUserById(userId); //Obtengo el usuario para inyectarlo en el modelo por si hace falta para la logica (siempre hace falta por lo general)

        viewModel.addAttribute("usuario", usuario);
        viewModel.addAttribute("provincias", Provincias.provincias);
        viewModel.addAttribute("evento", eventoEditar);

        return "editEvent";
    }

    //EDITAR EVENTO - PUT
    @PutMapping("/events/{idEvento}/edit") //Recordar que para usar put en thymeleaft hay que usar th:method
    public String editarEvento(@Valid @ModelAttribute("evento") Evento evento,
                               @PathVariable("idEvento") Long idEvento,
                               HttpSession sesion,
                               Model viewModel,
                               BindingResult result){

        Long userId = (Long) sesion.getAttribute("idLogueado");
        if(userId == null){
            return "redirect:/loginRegister";
        }
        User usuario = userService.findUserById(userId);
        if(result.hasErrors()){
            viewModel.addAttribute("usuario", usuario);
            viewModel.addAttribute("provincias", Provincias.provincias);
            return "editEvent";
        }
        //Seteo esto porque sino me crea un evento nuevo en vez de updatear
        evento.setOrganizador(usuario);
        evento.setId(idEvento);

        eventoService.editarEvento(evento);//Tranquilamente podria haber usado el metodo para crear un evento, ya que hacen lo mismo.
        return "redirect:/home";
    }

    //ELIMINAR EVENTO
    @DeleteMapping("/events/{id}/delete")
    public String eliminarEvento(@PathVariable("id") Long idEvento){
        eventoService.borrarEvento(idEvento);
        return "redirect:/home";
    }

}
