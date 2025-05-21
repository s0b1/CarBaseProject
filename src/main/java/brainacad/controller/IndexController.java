package brainacad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class IndexController
{
    @GetMapping("/home")
    public String index()
    {
        return "index"; // src/main/resources/templates/index.html
    }
}
