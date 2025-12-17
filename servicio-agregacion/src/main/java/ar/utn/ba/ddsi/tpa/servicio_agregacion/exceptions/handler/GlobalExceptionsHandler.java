package ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.handler;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.FuenteNoEncontradaException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.coleccion.ColeccionNoEncontradaException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.hecho.HechoNoEncontradoException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.solicitud.SolicitudNoEncontradaException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.solicitud.SolicitudSpamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {
    @ExceptionHandler(SolicitudSpamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleSolicitudSpam(SolicitudSpamException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HechoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleHechoNoEncontrado(HechoNoEncontradoException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ColeccionNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleColeccionNoEncontrada(ColeccionNoEncontradaException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FuenteNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleFuenteNoEncontrado(ColeccionNoEncontradaException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SolicitudNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleSolicitudNoEncontrado(SolicitudNoEncontradaException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleException(Exception e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
