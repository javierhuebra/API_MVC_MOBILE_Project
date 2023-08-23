package com.codingdojo.authentication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Email no es válido.")
    @NotBlank(message = "El mail no debe ser nulo")
    private String email;
    @NotBlank(message = "Debe seleccionar una provincia")
    private String provincia;
    @Size(min=5, message="El password debe ser de 6 caracteres como mínimo.")
    private String password;
    @Transient // Esto para no guardar en la base de datos la pass dos veces
    private String passwordConfirmation;
    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    // ORGANIZADOR DEL EVENTO
    //Relación 1 : N hacia Eventos (Un usuario puede organizar muchos eventos, un evento puede tener un solo usuario organizador)
    @OneToMany(mappedBy = "organizador",fetch = FetchType.LAZY)
    private List<Evento> eventosOrganizados;


    // ASISTENCIA A EVENTOS
    //Relacion N : N hacia Eventos (Un usuario puede asistir a muchos eventos y un evento puede tener muchos usuarios que asisten)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "asistentes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Evento> eventosAsistir;

    // MENSAJES EN EVENTOS
    //Relación 1 : N hacia Mensajes (Un usuario puede tener muchos mensajes pero un mensaje solo puede tener un usuario)
    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Mensaje> mensajes;
    public User() {
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public List<Evento> getEventosOrganizados() {
        return eventosOrganizados;
    }

    public void setEventosOrganizados(List<Evento> eventosOrganizados) {
        this.eventosOrganizados = eventosOrganizados;
    }

    public List<Evento> getEventosAsistir() {
        return eventosAsistir;
    }

    public void setEventosAsistir(List<Evento> eventosAsistir) {
        this.eventosAsistir = eventosAsistir;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
}
