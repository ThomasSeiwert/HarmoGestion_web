package fr.afpa.cda19.harmogestionweb.controllers;

import fr.afpa.cda19.harmogestionweb.models.Membre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
public class ControllerIndex {
    @GetMapping("/index")
    public static String pageIndex(Model model) {
        //Jeu d'essai
        ArrayList<Membre> listeMembres = new ArrayList<>();
        listeMembres.add(new Membre(1, "Brucker", "Rodolphe", LocalDate.of(2012, 4, 12)));
        listeMembres.add(new Membre(2, "Didier", "Cédric", LocalDate.of(2013, 8, 26)));
        listeMembres.add(new Membre(3, "Seiwert", "Thomas", LocalDate.of(2013, 11, 2)));
        listeMembres.add(new Membre(4, "Ugolini", "Cyril", LocalDate.of(2014, 7, 4)));
        listeMembres.add(new Membre(5, "Turbo", "Josianne", LocalDate.now()));
        //Affichage
        model.addAttribute("listeMembres", listeMembres);
        return "index";
    }
}
