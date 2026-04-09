package fr.afpa.cda19.harmogestionweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe de controller liée aux membres.
 */
@Controller
public class ControllerPagesCours {
    //==== Méthodes ====
    /**
     * Méthode d'accès à la page de planification de cours.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/planifierCours")
    public String planifierCours(Model model) {
        return "planifierCours";
    }

    /**
     * Méthode d'accès à la page des prochains cours.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/prochainsCours")
    public String prochainsCours(Model model) {
        return "prochainsCours";
    }
}
