package ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.fuente;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Entity
@Getter
@Setter
public class Fuente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombreDataset;
    @Transient
    private InputStream datasetInputStream;

    public Fuente(String nombreDataset, InputStream datasetInputStream) {
        this.nombreDataset = nombreDataset;
        this.datasetInputStream = datasetInputStream;
    }

    public Fuente() {

    }

    public TipoDataset extensionDataset() {
        if (nombreDataset.endsWith(".csv")) {
            return TipoDataset.CSV;
        }
        return null;
    }
}
