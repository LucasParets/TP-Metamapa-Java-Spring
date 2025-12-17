package ar.utn.ba.ddsi.tpa.servicio_auth.models.dtos;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String nombreDeUsuario;
    private String email;
    private String contrasenia;
}
