package ar.utn.ba.ddsi.tpa.interfaz_web.dtos;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
}
