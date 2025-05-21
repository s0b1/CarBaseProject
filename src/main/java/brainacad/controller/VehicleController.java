package brainacad.controller;

import brainacad.model.Vehicle;
import brainacad.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController
{

    private final VehicleService vehicleService;

    @GetMapping
    public String listVehicles(Model model)
    {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "vehicles/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model)
    {
        model.addAttribute("vehicle", new Vehicle());
        return "vehicles/add";
    }

    @PostMapping("/add")
    public String addVehicle(@ModelAttribute Vehicle vehicle)
    {
        vehicle.setAvailable(true);
        if (vehicle.getCondition() == null || vehicle.getCondition().isBlank())
        {
            vehicle.setCondition("GOOD");
        }
        vehicleService.saveVehicle(vehicle);
        return "redirect:/vehicles";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable Long id, Model model)
    {
        Vehicle vehicle = vehicleService.getVehicleById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        model.addAttribute("vehicle", vehicle);
        return "vehicles/delete";
    }


    @PostMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/vehicles";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model)
    {
        Vehicle vehicle = vehicleService.getVehicleById(id).orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID: " + id));
        model.addAttribute("vehicle", vehicle);
        return "vehicles/edit";
    }

    @PostMapping("/edit")
    public String updateVehicle(@ModelAttribute Vehicle vehicle)
    {
        vehicleService.saveVehicle(vehicle);
        return "redirect:/vehicles";
    }

}
