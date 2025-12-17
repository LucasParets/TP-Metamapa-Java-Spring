package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dashboard.DashboardDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.admin.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String home() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard(Model model) {
        try {
            DashboardDTO dto = dashboardService.getDashboard();
            model.addAttribute("titulo", "Dashboard");
            model.addAttribute("dashboard", dto);
            return "admin/dashboard";
        }
        catch (Exception e) {
            model.addAttribute("titulo", "Dashboard");
            model.addAttribute("error", "Error al cargar el dashboard.");
            System.out.println(e.getMessage() + e.getCause()); // Cambiarlo por logs
            return "admin/dashboard";
        }
    }

}
