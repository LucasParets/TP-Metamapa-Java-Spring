package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartamentoRepository extends JpaRepository<Departamento, String> {
}
