package com.codingdojo.authentication.controllers;

import com.codingdojo.authentication.clases.Provincias;
import com.codingdojo.authentication.models.Evento;
import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.services.EventoService;
import com.codingdojo.authentication.services.MensajeService;
import com.codingdojo.authentication.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EventoController {

    private final EventoService eventoService;
    private final UserService userService;

    private final MensajeService mensajeService;

    public EventoController(EventoService eventoService, UserService userService, MensajeService mensajeService){
        this.eventoService = eventoService;
        this.userService = userService;
        this.mensajeService = mensajeService;
    }

    //REGISTRAR UN EVENTO - POST
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

    //ELIMINAR EVENTO - DELETE
    @DeleteMapping("/events/{id}/delete")
    public String eliminarEvento(@PathVariable("id") Long idEvento){
        eventoService.borrarEvento(idEvento);
        return "redirect:/home";
    }

    //UNIRSE O CANCELAR ASISTENCIA - GET
    @GetMapping("/event/{idEvento}/{idUsuario}/{opcion}") //No hace falta mandar el idUsuario pero lo mando igual para la ruta
    public String asistirCancelarEvento(@PathVariable("idEvento") Long idEvento,
                                        @PathVariable("idUsuario") Long idUsuario,
                                        @PathVariable("opcion") String opcion,
                                        HttpSession sesion){

        Long userId = (Long) sesion.getAttribute("idLogueado");
        if(userId == null){
            return "redirect:/loginRegister";
        }
        Evento eventoSeleccionado = eventoService.buscarEvento(idEvento);
        boolean unirseCancelar = (opcion.equals("unirse"));
        User usuario = userService.findUserById(userId);

        eventoService.unirseCancelarEvento(eventoSeleccionado,usuario,unirseCancelar);
        return "redirect:/home";
    }

    //MOSTRAR DETALLE DE EVENTO - GET
    @GetMapping("/events/{idEvento}")
    public String mostrarEvento(Model viewModel,
                                @PathVariable("idEvento") Long idEvento,
                                HttpSession sesion){

        Long userId = (Long) sesion.getAttribute("idLogueado");
        if(userId == null){
            return "redirect:/loginRegister";
        }
        viewModel.addAttribute("evento", eventoService.buscarEvento(idEvento));
        return "showEvent";
    }

    //AGREGAR MENSAJE EN EL EVENTO - POST - Esto maneja los comentarios, se podria crear una nueva clase ComentariosController para tenerlo mas organizado y poder hacer otras operaciones con los comentarios.
    @PostMapping("/events/{idEvento}/comentario")
    public String crearComentario(@PathVariable("idEvento") Long idEvento,
                                  HttpSession sesion,
                                  RedirectAttributes errores,
                                  @RequestParam("comentario") String comentario){
        Long userId = (Long) sesion.getAttribute("idLogueado");
        if(userId == null){
            return "redirect:/loginRegister";
        }

        if(comentario.equals("")){
            errores.addFlashAttribute("error", "No se puede enviar mensajes vacios");
            return "redirect:/events/"+idEvento;
        }

        Evento eventoComentado = eventoService.buscarEvento(idEvento);
        User usuarioAutor = userService.findUserById(userId);

        mensajeService.agregarComentario(usuarioAutor,eventoComentado,comentario);
        return "redirect:/events/"+idEvento;
    }
}
