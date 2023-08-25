package com.codingdojo.authentication.services;

import com.codingdojo.authentication.models.Evento;
import com.codingdojo.authentication.models.Mensaje;
import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.repositories.MensajeRepository;
import org.springframework.stereotype.Service;

@Service
public class MensajeService {
    private final MensajeRepository mensajeRepository;

    public MensajeService(MensajeRepository mensajeRepository){
        this.mensajeRepository = mensajeRepository;
    }

    //CREAR UN MENSAJE
    public void agregarComentario(User usuario, Evento evento, String comentario){
        Mensaje mensaje = new Mensaje(comentario,usuario,evento);
        mensajeRepository.save(mensaje);
    }
}
