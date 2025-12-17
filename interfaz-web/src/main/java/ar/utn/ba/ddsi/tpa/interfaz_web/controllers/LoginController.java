package ar.utn.ba.ddsi.tpa.interfaz_web.controllers;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.AuthResponseDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.UsuarioDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@AllArgsConstructor
public class LoginController {
    private final MetaMapaApiService api;
    private final AuthenticationManager authManager;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("titulo", "Login");
        return "metamapa/login";
    }


    @GetMapping("/registrarse")
    public String formRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("titulo", "Registrarse");
        return "metamapa/registrarse";
    }

    @PostMapping("/registrarse")
    public String registrar(@ModelAttribute("usuario") UsuarioDTO usuario,
                            BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "metamapa/registrarse";
        try {
            api.registrarUsuario(usuario);

            var auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Te has registrado correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/inicio";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "Error",
                    "text", "Error al registrar el usuario.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/registrarse";
        } catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "Error",
                    "text", "Error al registrar el usuario.",
                    "confirmButtonText", "Aceptar"
            ));
            return "metamapa/registrarse";
        }
    }
}
