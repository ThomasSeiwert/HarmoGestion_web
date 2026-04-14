package fr.afpa.cda19.harmogestionweb.controllers;

import fr.afpa.cda19.harmogestionweb.models.Membre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

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
        model.addAttribute("titrePage", "Fiche membre");
        String tokenCSRF = generationToken();
        model.addAttribute("csrfToken", tokenCSRF);
        model.addAttribute("csrfTokenServer", tokenCSRF);
        jeuEssai(model);
        return "ficheMembre";
    }

    /**
     * Méthode d'accès à la page d'inscription de membre.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/inscriptionMembre")
    public String inscriptionMembre(Model model) {
        model.addAttribute("titrePage", "Inscription membre");
        String tokenCSRF = generationToken();
        model.addAttribute("csrfToken", tokenCSRF);
        model.addAttribute("csrfTokenServer", tokenCSRF);
        return "inscriptionMembre";
    }

    /**
     * Méthode d'accès à la page de la liste des membres.
     * @param model Modèle de la page.
     * @return URI de la page.
     */
    @GetMapping("/listeMembres")
    public String listeMembres(Model model) {
        jeuEssai(model);
        model.addAttribute("titrePage", "Liste des membres");
        return "listeMembres";
    }

    /**
     * Affichage du jeu d'essai des membres sur la page d'index.
     * Cette méthode est là uniquement à des fins d'exemple et ne devra pas faire
     * partie du projet final.
     * @param model Modèle de la page.
     */
    void jeuEssai(Model model) {
        //Jeu d'essai
        ArrayList<Membre> listeMembres = new ArrayList<>();
        listeMembres.add(new Membre(1, "Brucker", "Rodolphe", LocalDate.of(2012, 4, 12)));
        listeMembres.add(new Membre(2, "Didier", "Cédric", LocalDate.of(2013, 8, 26)));
        listeMembres.add(new Membre(3, "Seiwert", "Thomas", LocalDate.of(2013, 11, 2)));
        listeMembres.add(new Membre(4, "Ugolini", "Cyril", LocalDate.of(2014, 7, 4)));
        listeMembres.add(new Membre(5, "Turbo", "Josianne", LocalDate.now()));
        //Affichage
        model.addAttribute("listeMembres", listeMembres);
    }

    /**
     * Génère un token CSRF.
     */
    String generationToken() {
        String generation = UUID.randomUUID() + UUID.randomUUID().toString() + UUID.randomUUID();
        String csrfToken = generation.substring(0, 100);
        return csrfToken;
    }
}
