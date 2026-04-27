package fr.afpa.cda19.harmogestionweb.controllers;

import fr.afpa.cda19.harmogestionweb.exceptions.ControllerException;
import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Cours;
import fr.afpa.cda19.harmogestionweb.models.Instrument;
import fr.afpa.cda19.harmogestionweb.models.Membre;
import fr.afpa.cda19.harmogestionweb.services.CoursService;
import fr.afpa.cda19.harmogestionweb.services.InstrumentService;
import fr.afpa.cda19.harmogestionweb.services.MembreService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Classe de controller liée aux membres.
 */
@Controller
public class ControllerPagesCours {

    //--------------------------------------------------------------------------
    // Attributs
    //--------------------------------------------------------------------------

    /**
     * Instance du service des cours.
     */
    private final CoursService coursService;

    /**
     * Instance du service des instruments.
     */
    private final InstrumentService instrumentService;

    /**
     * Instance du service des membres.
     */
    private final MembreService membreService;

    private static final String ACTION = "action";
    private static final String NOM_SUBMIT = "nomSubmit";
    private static final String TITRE_FORM = "titreFormulaire";
    private static final String TITRE_PAGE = "titrePage";
    private static final String COURS = "cours";
    private static final String INSTRUMENTS = "instruments";
    private static final String MEMBRES = "membres";
    private static final String STATUT = "statut";
    private static final String GERER_COURS = "gererCours";
    private static final String CREER_UN_COURS = "Créer un cours";
    private static final String MODIFIER_UN_COURS = "Modifier un cours";
    private static final String SUPPRIMER_UN_COURS = "Supprimer un cours";
    private static final String URL_REDIRECT = "redirect:/prochainsCours";

    //--------------------------------------------------------------------------
    // Constructeurs
    //--------------------------------------------------------------------------

    /**
     * Constructeur du controller des pages des cours.
     *
     * @param coursService CoursService : service des cours.
     */
    @Autowired
    public ControllerPagesCours(final CoursService coursService,
                                final InstrumentService instrumentService,
                                final MembreService membreService) {

        this.coursService = coursService;
        this.instrumentService = instrumentService;
        this.membreService = membreService;
    }

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Méthode d'accès à la page de création de cours vierge.
     *
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/creerCours")
    public String creerCours(Model model) {

        try {
            Iterable<Instrument> instruments = instrumentService.getInstruments();
            Iterable<Membre> membres = membreService.getMembres();

            model.addAttribute(ACTION, "/creerCours");
            model.addAttribute(NOM_SUBMIT, "Créer");
            model.addAttribute(TITRE_FORM, CREER_UN_COURS);
            model.addAttribute(TITRE_PAGE, CREER_UN_COURS);
            model.addAttribute(COURS, new Cours());
            model.addAttribute(INSTRUMENTS, instruments);
            model.addAttribute(MEMBRES, membres);

            return GERER_COURS;
        }
        catch (RepositoryException _) {
            throw new ControllerException("Erreur inconnue");
        }
    }

    /**
     * Méthode d'accès à la page de création de cours pour un retour de formulaire.
     *
     * @param cours Cours : cours entré dans le formulaire
     * @param model Modèle de la page
     *
     * @return page de formulaire si erreur, redirect aux prochains cours si succès
     */
    @PostMapping("/creerCours")
    public ModelAndView creerCours(
            @ModelAttribute final Cours cours, ModelMap model) {

        try {
            cours.setInstrument(instrumentService.getInstrument(cours.getInstrument().getIdInstrument()));
            cours.setEnseignant(membreService.getMembre(cours.getEnseignant().getIdMembre()));
            ArrayList<Membre> participants = new ArrayList<>();
            for (Membre membre : cours.getParticipants()) {
                membre = membreService.getMembre(membre.getIdMembre());
                participants.add(membre);
            }
            cours.setParticipants(participants);

            Set<ConstraintViolation<Cours>> erreurs = getErreurs(cours);

            if (!erreurs.isEmpty()) {
                Iterable<Instrument> instruments = instrumentService.getInstruments();
                Iterable<Membre> membres = membreService.getMembres();
                model.addAttribute(ACTION, "/creerCours");
                model.addAttribute(NOM_SUBMIT, "Créer");
                model.addAttribute(TITRE_FORM, CREER_UN_COURS);
                model.addAttribute(TITRE_PAGE, CREER_UN_COURS);
                model.addAttribute(COURS, cours);
                model.addAttribute(INSTRUMENTS, instruments);
                model.addAttribute(MEMBRES, membres);

                return new ModelAndView(GERER_COURS, model);
            }
            else {
                coursService.saveCours(cours);
                model.addAttribute(STATUT, "Création réussie");
                return new ModelAndView(URL_REDIRECT, model);
            }
        }
        catch (RepositoryException _) {
            throw new ControllerException("Erreur inconnue");
        }
    }

    /**
     * Méthode d'accès à la page de modification d'un cours (1er envoi).
     *
     * @param id    Identifiant du cours à modifier
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/modifierCours/{id}")
    public String modifierCours(
            @PathVariable final int id,
            Model model) {

        try {
            Cours cours = coursService.getCours(id);

            Iterable<Instrument> instruments = instrumentService.getInstruments();
            Iterable<Membre> membres = membreService.getMembres();

            model.addAttribute(ACTION, "/modifierCours/" + id);
            model.addAttribute(NOM_SUBMIT, "Modifier");
            model.addAttribute(TITRE_FORM, MODIFIER_UN_COURS);
            model.addAttribute(TITRE_PAGE, MODIFIER_UN_COURS);
            model.addAttribute(COURS, cours);
            model.addAttribute(INSTRUMENTS, instruments);
            model.addAttribute(MEMBRES, membres);

            return GERER_COURS;
        }
        catch (RepositoryException _) {
            throw new ControllerException("Erreur inconnue");
        }
    }

    /**
     * Méthode d'accès à la page de modification de cours pour retour de formulaire.
     *
     * @param id    int : id du cours
     * @param cours Cours : le cours
     * @param model Modèle de la page
     *
     * @return page de formulaire si erreur, redirect aux prochains cours si succès
     */
    @PostMapping("/modifierCours/{id}")
    public ModelAndView modifierCours(
            @PathVariable final int id,
            @ModelAttribute final Cours cours, ModelMap model) {

        try {
            cours.setInstrument(instrumentService.getInstrument(cours.getInstrument().getIdInstrument()));
            cours.setEnseignant(membreService.getMembre(cours.getEnseignant().getIdMembre()));
            ArrayList<Membre> participants = new ArrayList<>();
            for (Membre membre : cours.getParticipants()) {
                membre = membreService.getMembre(membre.getIdMembre());
                participants.add(membre);
            }
            cours.setParticipants(participants);

            Set<ConstraintViolation<Cours>> erreurs = getErreurs(cours);

            if (!erreurs.isEmpty()) {
                Iterable<Instrument> instruments = instrumentService.getInstruments();
                Iterable<Membre> membres = membreService.getMembres();
                model.addAttribute(ACTION, "/modifierCours/" + id);
                model.addAttribute(NOM_SUBMIT, "Modifier");
                model.addAttribute(TITRE_FORM, MODIFIER_UN_COURS);
                model.addAttribute(TITRE_PAGE, MODIFIER_UN_COURS);
                model.addAttribute(COURS, cours);
                model.addAttribute(INSTRUMENTS, instruments);
                model.addAttribute(MEMBRES, membres);

                return new ModelAndView(GERER_COURS, model);
            }
            else {
                coursService.saveCours(cours);
                model.addAttribute(STATUT, "Modification réussie");
                return new ModelAndView(URL_REDIRECT, model);
            }
        }
        catch (RepositoryException _) {
            throw new ControllerException("Erreur inconnue");
        }
    }

    /**
     * Méthode d'accès à la page de suppression d'un cours (1er envoi).
     *
     * @param id    Identifiant du cours à supprimer
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/supprimerCours/{id}")
    public String supprimerCours(
            @PathVariable final int id,
            Model model) {

        try {
            Cours cours = coursService.getCours(id);

            Iterable<Instrument> instruments = instrumentService.getInstruments();
            Iterable<Membre> membres = membreService.getMembres();

            model.addAttribute(ACTION, "/supprimerCours/" + id);
            model.addAttribute(NOM_SUBMIT, "Supprimer");
            model.addAttribute(TITRE_FORM, SUPPRIMER_UN_COURS);
            model.addAttribute(TITRE_PAGE, SUPPRIMER_UN_COURS);
            model.addAttribute(COURS, cours);
            model.addAttribute(INSTRUMENTS, instruments);
            model.addAttribute(MEMBRES, membres);

            return GERER_COURS;
        }
        catch (RepositoryException _) {
            throw new ControllerException("Erreur inconnue");
        }
    }

    /**
     * Méthode d'accès à la page de suppression de cours pour retour de formulaire.
     *
     * @param id    int : id du cours
     * @param model Modèle de la page
     *
     * @return redirect à la selection de cours
     */
    @PostMapping("/supprimerCours/{id}")
    public ModelAndView supprimerCours(
            @PathVariable final int id,
            ModelMap model) {

        try {
            coursService.deleteCours(id);
            model.addAttribute(STATUT, "Suppression réussie");

            return new ModelAndView(URL_REDIRECT, model);
        }
        catch (RepositoryException _) {
            throw new ControllerException("Erreur inconnue");
        }
    }

    /**
     * Méthode d'accès à la page des prochains cours.
     *
     * @param model Modèle de la page.
     *
     * @return URI de la page.
     */
    @GetMapping("/prochainsCours")
    public String prochainsCours(Model model) {

        try {
            ArrayList<Cours> listeCours =
                    (ArrayList<Cours>) coursService.getProchainsCours();

            // si un cours a été trouvé, on affiche les 3 prochains cours au max
            listeCours.sort(Cours.COMPARATOR_DATE);
            ArrayList<Cours> prochainsCours = new ArrayList<>();
            int i = 0;
            while (prochainsCours.size() <= 3 && i < listeCours.size()) {
                Cours cours = listeCours.get(i);
                prochainsCours.add(cours);
                i++;
            }
            model.addAttribute("prochainsCours", prochainsCours);
        }
        catch (RepositoryException _) {
            // si aucun cours n'a été trouvé, on affiche un message
            model.addAttribute("aucunCours",
                    "Il n'y a pas de prochains cours pour le moment");
        }
        model.addAttribute(TITRE_PAGE, "Prochains Cours");

        return "prochainsCours";
    }

    /**
     * Méthode pour récupérer les erreurs du cours avec le validator.
     *
     * @param cours Cours : le cours à valider.
     *
     * @return Set : liste des erreurs de saisies pour le cours.
     */
    private static Set<ConstraintViolation<Cours>> getErreurs(
            final Cours cours) {

        try (ValidatorFactory validatorFactory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator =
                    validatorFactory.getValidator();

            return validator.validate(cours);
        }
    }
}
