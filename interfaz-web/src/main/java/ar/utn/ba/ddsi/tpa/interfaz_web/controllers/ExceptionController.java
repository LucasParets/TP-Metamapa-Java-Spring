package ar.utn.ba.ddsi.tpa.interfaz_web.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class ExceptionController implements ErrorController {
    @RequestMapping("/error")
    public String error(HttpServletRequest request, RedirectAttributes ra) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "redirect:/inicio";
            }
        }

        ra.addFlashAttribute("swal", Map.of(
                "icon", "error",
                "title", "¡Error!",
                "text", "Se produjo un error. " + request.getAttribute(RequestDispatcher.ERROR_MESSAGE),
                "confirmButtonText", "Aceptar"
        ));
        return "redirect:/inicio";
    }
}
