package brainacad.controller;

import brainacad.model.AppUser;
import brainacad.repository.UserRepository;
import brainacad.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController
{

    private final AppUserService userService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login()
    {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model)
    {
        model.addAttribute("user", new AppUser());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model)
    {
        if (userRepository.findByUsername(username).isPresent())
        {
            model.addAttribute("error", "Username already exists.");
            model.addAttribute("user", new AppUser());
            return "auth/register";
        }

        userService.register(username, password);
        return "redirect:/register?success";
    }

}


