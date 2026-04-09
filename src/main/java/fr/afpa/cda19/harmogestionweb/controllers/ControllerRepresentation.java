package fr.afpa.cda19.harmogestionweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe de controller liée aux représentations.
 */
@Controller
public class ControllerRepresentation {
    //==== Méthodes ====
    /**
     * Méthode d'accès à la page des prochaines représentations.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/prochainesRepresentations")
    public String prochainesRepresentations(Model model) {
        return "prochainesRepresentations";
    }

    /**
     * Méthode d'accès à la page de planification d'une
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/planifierRepresentation")
    public String planifierRepresentation(Model model) {
        return "planifierRepresentation";
    }
}
