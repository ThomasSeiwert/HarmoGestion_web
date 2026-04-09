package fr.afpa.cda19.harmogestionweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe de controller liée aux membres.
 */
@Controller
public class ControllerPagesInstruments {
    //==== Méthodes ====
    /**
     * Méthode d'accès à la page d'ajout d'instrument'.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/ajouterInstrument")
    public String ajouterInstrument(Model model) {
        return "ajouterInstrument";
    }
}
