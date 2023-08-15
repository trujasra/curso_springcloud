package com.trujasra.msvc.usuarios.controllers;

import com.trujasra.msvc.usuarios.models.entity.Usuario;
import com.trujasra.msvc.usuarios.services.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@AllArgsConstructor
@RestController
public class UsuarioController {
    private UsuarioService service;

    @GetMapping("/")
    public Map<String,List<Usuario>> listar() {
        return Collections.singletonMap("user", service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok().body(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        //if (!usuario.getEmail().isEmpty() && service.porEmail(usuario.getEmail()).isPresent()) {
        if (!usuario.getEmail().isEmpty() && service.existePorEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe! un usuario con ese email electrónico!"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            Usuario usuarioBD = o.get();
            if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioBD.getEmail()) && service.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electronico."));
            }

            usuarioBD.setNombre(usuario.getNombre());
            usuarioBD.setEmail(usuario.getEmail());
            usuarioBD.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioBD));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCursos(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.listarPorIds(ids));
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), "el campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

}
