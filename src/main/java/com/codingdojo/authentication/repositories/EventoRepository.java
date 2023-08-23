package com.codingdojo.authentication.repositories;

import com.codingdojo.authentication.models.Evento;
import com.codingdojo.authentication.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventoRepository extends CrudRepository<Evento,Long> {
    List<Evento> findByProvincia(String provincia); //Buscar eventos que se hagan en la provincia dada

    List<Evento> findByProvinciaIsNot(String provincia); //Buscar eventos que no se hagan en la provincia dada

}
