package fr.afpa.cda19.harmogestionweb.controllers;

import fr.afpa.cda19.harmogestionweb.models.Membre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.ArrayList;

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
        jeuEssai(model);
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
}
