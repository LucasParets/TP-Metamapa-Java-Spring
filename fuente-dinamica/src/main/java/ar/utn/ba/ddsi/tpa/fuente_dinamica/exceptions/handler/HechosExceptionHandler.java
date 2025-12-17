package ar.utn.ba.ddsi.tpa.fuente_dinamica.exceptions.handler;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.exceptions.hechos.HechoNoEncontradoException;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.exceptions.hechos.ModificacionHechoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class HechosExceptionHandler {

    @ExceptionHandler(ModificacionHechoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleModificacionHecho(ModificacionHechoException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HechoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleHechoNoEncontrado(HechoNoEncontradoException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
