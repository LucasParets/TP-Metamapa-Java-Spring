package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.DatasetInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.admin.ImportarDatasetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/admin/importar")
public class ImportarDatasetController {
    private final ImportarDatasetService importarDatasetService;
    private static final Logger log = LoggerFactory.getLogger(ImportarDatasetController.class);

    public ImportarDatasetController(ImportarDatasetService importarDatasetService) {
        this.importarDatasetService = importarDatasetService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String cargarDataset(Model model) {
        model.addAttribute("titulo", "Importar dataset");
        model.addAttribute("dataset", new DatasetInputDTO());
        return "admin/importar_dataset";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String importarDataset(@RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        log.info("Controller recibió archivo: {}", file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("swal", Map.of(
                        "icon", "warning",
                        "title", "¡Cuidado!",
                        "text", "Por favor, seleccione un archivo para importar.",
                        "confirmButtonText", "Aceptar"
                ));
                return "redirect:/admin/importar";
            }

            importarDatasetService.importarDataset(file);
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se cargó el archivo correctamente, está siendo procesado.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al cargar el archivo.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/importar";
        }
    }

}
