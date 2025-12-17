package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DatasetInputDTO {
    private MultipartFile file;
}
