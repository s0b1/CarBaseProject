package brainacad.controller;


import brainacad.model.Driver;
import brainacad.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public String listDrivers(Model model)
    {
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "drivers/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model)
    {
        model.addAttribute("driver", new Driver());
        return "drivers/add";
    }

    @PostMapping("/add")
    public String addDriver(@ModelAttribute Driver driver)
    {
        driver.setAvailable(true);
        driver.setTotalEarnings(java.math.BigDecimal.ZERO);
        driverService.saveDriver(driver);
        return "redirect:/drivers";
    }

    @GetMapping("/delete/{id}")
    public String deleteDriver(@PathVariable Long id)
    {
        driverService.deleteDriver(id);
        return "redirect:/drivers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model)
    {
        Driver driver = driverService.getDriverById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid driver ID: " + id));
        model.addAttribute("driver", driver);
        return "drivers/edit";
    }

    @PostMapping("/edit")
    public String updateDriver(@ModelAttribute Driver driver)
    {
        driverService.saveDriver(driver);
        return "redirect:/drivers";
    }


}
