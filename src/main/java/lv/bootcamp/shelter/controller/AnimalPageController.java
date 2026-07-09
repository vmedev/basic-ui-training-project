package lv.bootcamp.shelter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.form.AnimalForm;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AnimalPageController {
    private final AnimalService animalService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/animals")
    public String listAnimals(@RequestParam(required = false)AnimalType type, Model model) {
        model.addAttribute("animals", animalService.findAll(type));
        return "animals";
    }

    @GetMapping("/animals/new")
    public String newAnimalForm(Model model) {
        if (!isAdmin()) {
            return "redirect:/animals";
        }
        model.addAttribute("form", new AnimalForm(null, null, null, null, null, null));
        return "animals-new";
    }

    @GetMapping("/animals/{id}")
    public String animalDetail(@PathVariable Long id, Model model) {
        AnimalResponse animal = animalService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal not found"));
        model.addAttribute("animal", animal);
        return "animal";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/animals")
    public String createAnimal(@Valid @ModelAttribute("form") AnimalForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "animals-new";
        }
        animalService.createFromForm(form);
        redirectAttributes.addFlashAttribute("message", "Animal added!");
        return "redirect:/animals";
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    @ModelAttribute("isUser")
    public boolean isUser() {
        return hasRole("ROLE_USER");
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }


}
