package com.codingdojo.authentication.repositories;

import com.codingdojo.authentication.models.Mensaje;
import org.springframework.data.repository.CrudRepository;

public interface MensajeRepository extends CrudRepository<Mensaje,Long> {
}
