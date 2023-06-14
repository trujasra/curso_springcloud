package com.trujasra.springcloud.msvc.cursos.repositories;

import com.trujasra.springcloud.msvc.cursos.entity.Curso;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends CrudRepository<Curso, Long> {
}
