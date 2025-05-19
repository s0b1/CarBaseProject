package brainacad.controller;

import brainacad.model.CargoRequest;
import brainacad.service.CargoRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cargo")
@RequiredArgsConstructor
public class CargoRequestController
{

    private final CargoRequestService cargoRequestService;

    @GetMapping
    public String listRequests(Model model)
    {
        model.addAttribute("requests", cargoRequestService.getAllRequests());
        return "cargo/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model)
    {
        model.addAttribute("cargoRequest", new CargoRequest());
        return "cargo/add";
    }

    @PostMapping("/add")
    public String addCargoRequest(@ModelAttribute CargoRequest cargoRequest)
    {
        cargoRequestService.saveRequest(cargoRequest);
        return "redirect:/cargo";
    }

    @GetMapping("/delete/{id}")
    public String deleteRequest(@PathVariable Long id)
    {
        cargoRequestService.deleteRequest(id);
        return "redirect:/cargo";
    }
}
