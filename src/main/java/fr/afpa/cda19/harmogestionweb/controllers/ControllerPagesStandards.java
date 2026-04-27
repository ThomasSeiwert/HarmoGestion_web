package fr.afpa.cda19.harmogestionweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe de controller liée aux pages standards (index, accessibilité, mentions légales).
 */
@Controller
public class ControllerPagesStandards {
    /**
     * Méthode d'accès à la page d'index'.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    /**
     * Méthode d'accès à la page relative à l'accessibilité.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/accessibilite")
    public String accessibilite(Model model) {
        return "accessibilite";
    }

    /**
     * Méthode d'accès à la page des mentions légales.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/mentionsLegales")
    public String mentionsLegales(Model model) {
        return "mentionsLegales";
    }
}
