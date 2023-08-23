package com.codingdojo.authentication.services;

import com.codingdojo.authentication.models.Evento;
import com.codingdojo.authentication.repositories.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {
    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository){
        this.eventoRepository= eventoRepository;
    }

    public Evento crearEvento(Evento evento){
        return eventoRepository.save(evento);
    }

    public List<Evento> buscarEventosPorProvincia(String provincia){
        return eventoRepository.findByProvincia(provincia);
    }

    public List<Evento> buscarEventosNotProvincia(String provincia){
        return eventoRepository.findByProvinciaIsNot(provincia);
    }


}
