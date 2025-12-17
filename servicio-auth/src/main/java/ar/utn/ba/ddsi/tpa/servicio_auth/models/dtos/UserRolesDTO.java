package ar.utn.ba.ddsi.tpa.servicio_auth.models.dtos;

import ar.utn.ba.ddsi.tpa.servicio_auth.models.entities.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesDTO {
    private String username;
    private Rol rol;
}
