package fr.afpa.cda19.harmogestionweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe de controller liée aux représentations.
 */
@Controller
public class ControllerRepresentation {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

    private static final String TITRE_PAGE = "titrePage";

    //----------------------------------------------------------------------------------------------
    // Méthodes
    //----------------------------------------------------------------------------------------------

    /**
     * Méthode d'accès à la page des prochaines représentations.
     *
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/prochainesRepresentations")
    public String prochainesRepresentations(Model model) {

        model.addAttribute(TITRE_PAGE, "Prochaines Représentations");

        return "prochainesRepresentations";
    }

    /**
     * Méthode d'accès à la page de planification d'une représentation.
     *
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/planifierRepresentation")
    public String planifierRepresentation(Model model) {

        model.addAttribute(TITRE_PAGE, "Créer représentation");

        return "formRepresentation";
    }
}
