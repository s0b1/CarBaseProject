package brainacad.controller;

import brainacad.service.RouteService;
import brainacad.model.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController
{

    private final RouteService routeService;

    @GetMapping
    public String listRoutes(Model model)
    {
        List<Route> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);
        return "routes/list";
    }

    @GetMapping("/complete/{id}")
    public String markComplete(@PathVariable Long id)
    {
        try {
            routeService.completeRoute(id);
        } catch (Exception e) {
            System.err.println("Error completing route: " + e.getMessage());
        }
        return "redirect:/routes";
    }
}
