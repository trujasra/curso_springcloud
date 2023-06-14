package com.trujasra.springcloud.msvc.cursos.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

}
