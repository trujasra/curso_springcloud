package com.trujasra.msvc.usuarios.models.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Column(unique = true)
    private String email;
    private String password;
}
