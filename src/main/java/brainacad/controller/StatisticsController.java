package brainacad.controller;

import brainacad.service.StatisticsService;
import brainacad.model.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class StatisticsController
{

    private final StatisticsService statisticsService;

    @GetMapping("/stats")
    public String showStats(Model model)
    {
        Optional<Driver> topDriverOpt = statisticsService.getTopEarningDriver();

        Map<String, String> earningsPerDriver = statisticsService.getEarningsPerDriver();
        Map<String, Double> cargoPerDestination = statisticsService.getCargoWeightPerDestination();
        Map<String, Double> cargoPerDriver = statisticsService.getCargoWeightPerDriver();

        model.addAttribute("topDriver", topDriverOpt.orElse(null));
        model.addAttribute("earningsPerDriver", earningsPerDriver != null ? earningsPerDriver : Collections.emptyMap());
        model.addAttribute("cargoPerDestination", cargoPerDestination != null ? cargoPerDestination : Collections.emptyMap());
        model.addAttribute("cargoPerDriver", cargoPerDriver != null ? cargoPerDriver : Collections.emptyMap());

        return "stats/dashboard";
    }
}
