package fr.afpa.cda19.harmogestionweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe de controller liée aux membres.
 */
@Controller
public class ControllerPagesMembres {
    //==== Méthodes ====
    /**
     * Méthode d'accès à la page de fiche de membre.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/ficheMembre")
    public String ficheMembre(Model model) {
        return "ficheMembre";
    }

    /**
     * Méthode d'accès à la page d'inscription de membre.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/inscriptionMembre")
    public String inscriptionMembre(Model model) {
        return "inscriptionMembre";
    }

    /**
     * Méthode d'accès à la page de la liste des membres.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/listeMembres")
    public String listeMembres(Model model) {
        return "listeMembres";
    }
}
