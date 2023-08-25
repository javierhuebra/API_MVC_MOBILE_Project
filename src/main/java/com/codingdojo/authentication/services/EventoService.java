package com.codingdojo.authentication.services;

import com.codingdojo.authentication.models.Evento;
import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.repositories.EventoRepository;
import jdk.jfr.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {
    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository){
        this.eventoRepository= eventoRepository;
    }

    //CREAR EVENTO
    public Evento crearEvento(Evento evento){
        return eventoRepository.save(evento);
    }

    //EDITAR EVENTO
    public Evento editarEvento(Evento evento){
        return eventoRepository.save(evento);
    }

    //ELIMINAR EVENTO
    public void borrarEvento(Long id){
        eventoRepository.deleteById(id);
    }

    //RETORNAR UN EVENTO POR ID
    public Evento buscarEvento(Long id){
        return eventoRepository.findById(id).orElse(null);
    }

    //RETORNAR EVENTOS POR PROVINCIA
    public List<Evento> buscarEventosPorProvincia(String provincia){
        return eventoRepository.findByProvincia(provincia);
    }

    //RETORNAR EVENTOS POR PROVINCIA NEGADO
    public List<Evento> buscarEventosNotProvincia(String provincia){
        return eventoRepository.findByProvinciaIsNot(provincia);
    }

    //CONFIRMAR O CANCELAR ASISTENCIA A EVENTO
    public void unirseCancelarEvento(Evento evento, User usuario, boolean confirmacion){
        if(confirmacion){
            evento.getAsistentes().add(usuario);
        }else{
            evento.getAsistentes().remove(usuario);
        }
        eventoRepository.save(evento);//Una vez modificada la lista de asistentes guardo el mismo evento nuevamente.
    }




}
