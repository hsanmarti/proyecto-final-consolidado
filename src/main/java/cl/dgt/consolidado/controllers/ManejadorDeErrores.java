package cl.dgt.consolidado.controllers;

import cl.dgt.consolidado.services.ContribuyenteNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class ManejadorDeErrores {

    @ExceptionHandler(ContribuyenteNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(ContribuyenteNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", e.getMessage()));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class,
                       MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, String>> parametroMalo(Exception e) {
        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", "Faltan `desde` y `hasta`, o no tienen formato YYYY-MM-DD"));
    }
}
