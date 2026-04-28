package fr.afpa.cda19.harmogestionweb.controllers;

import fr.afpa.cda19.harmogestionweb.dto.CoursDto;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Classe de controller liée aux membres.
 */
@Controller
@Log4j2
public class ControllerPagesCours {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

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
    private static final String STATUT = "statut";
    private static final String FORM_COURS = "formCours";
    private static final String URL_REDIRECT = "redirect:/prochainsCours";

    //----------------------------------------------------------------------------------------------
    // Constructeurs
    //----------------------------------------------------------------------------------------------

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

    //----------------------------------------------------------------------------------------------
    // Endpoints
    //----------------------------------------------------------------------------------------------

    /**
     * Méthode d'accès à la page de création de cours vierge.
     *
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/creerCours")
    public String creerCoursGet(ModelMap model) {

        try {
            setAttributsCreation(model, new Cours(), null, null, new ArrayList<>());

            return FORM_COURS;
        }
        catch (RepositoryException _) {
            throw new ControllerException("Erreur inconnue");
        }
    }

    /**
     * Méthode d'accès à la page de création de cours pour un retour de formulaire.
     *
     * @param idParticipants id des participants du cours
     * @param coursDto       Cours : cours entré dans le formulaire
     * @param model          Modèle de la page
     *
     * @return page de formulaire si erreur, redirect aux prochains cours si succès
     */
    @PostMapping("/creerCours")
    public ModelAndView creerCoursPost(
            @RequestParam("instrument") int idInstrument,
            @RequestParam("enseignant") int idEnseignant,
            @RequestParam("participants") List<Integer> idParticipants,
            @ModelAttribute final CoursDto coursDto,
            ModelMap model) {

        try {
            Cours cours = completerCours(coursDto, idInstrument, idEnseignant, idParticipants);

            Set<ConstraintViolation<Cours>> erreurs = getErreurs(cours);
            setMessagesErreur(erreurs, model);

            if (!erreurs.isEmpty()) {
                setAttributsCreation(model, cours, idInstrument, idEnseignant, idParticipants);

                return new ModelAndView(FORM_COURS, model);
            }
            else {
                coursService.saveCours(cours);
                model.addAttribute(STATUT, "created");

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
    public String modifierCoursGet(
            @PathVariable final int id,
            ModelMap model) {

        try {
            Cours cours = coursService.getCours(id);
            ArrayList<Integer> idParticipants = new ArrayList<>();
            for (Membre membre : cours.getParticipants()) {
                idParticipants.add(membre.getIdMembre());
            }

            setAttributsModification(model, cours, cours.getInstrument().getIdInstrument(),
                    cours.getEnseignant().getIdMembre(), idParticipants, id);

            return FORM_COURS;
        }
        catch (RepositoryException _) {
            throw new ControllerException("Erreur inconnue");
        }
    }

    /**
     * Méthode d'accès à la page de modification de cours pour retour de formulaire.
     *
     * @param id       int : id du cours
     * @param coursDto Cours : le cours
     * @param model    Modèle de la page
     *
     * @return page de formulaire si erreur, redirect aux prochains cours si succès
     */
    @PostMapping("/modifierCours/{id}")
    public ModelAndView modifierCoursPost(
            @PathVariable final int id,
            @RequestParam("instrument") int idInstrument,
            @RequestParam("enseignant") int idEnseignant,
            @RequestParam("participants") List<Integer> idParticipants,
            @ModelAttribute final CoursDto coursDto,
            ModelMap model) {

        try {
            Cours cours = completerCours(coursDto, idInstrument, idEnseignant, idParticipants);

            Set<ConstraintViolation<Cours>> erreurs = getErreurs(cours);
            setMessagesErreur(erreurs, model);

            if (!erreurs.isEmpty()) {
                setAttributsModification(model, cours, idInstrument, idEnseignant,
                        idParticipants, id);

                return new ModelAndView(FORM_COURS, model);
            }
            else {
                coursService.saveCours(cours);
                model.addAttribute(STATUT, "updated");

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
    public String supprimerCoursGet(
            @PathVariable final int id,
            ModelMap model) {

        try {
            Cours cours = coursService.getCours(id);
            ArrayList<Integer> idParticipants = new ArrayList<>();
            for (Membre membre : cours.getParticipants()) {
                idParticipants.add(membre.getIdMembre());
            }

            setAttributsSuppression(model, cours, cours.getInstrument().getIdInstrument(),
                    cours.getEnseignant().getIdMembre(), idParticipants, id);

            return FORM_COURS;
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
    public ModelAndView supprimerCoursPost(
            @PathVariable final int id,
            ModelMap model) {

        try {
            coursService.deleteCours(id);
            model.addAttribute(STATUT, "deleted");

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
    public String prochainsCours(
            @RequestParam(required = false) Optional<String> statut,
            Model model) {

        if (statut.isPresent()) {
        switch (statut.get()) {
            case "created":
                model.addAttribute(STATUT, "Création réussie");
                break;
            case "updated":
                model.addAttribute(STATUT, "Modification réussie");
                break;
            case "deleted":
                model.addAttribute(STATUT, "Suppression réussie");
                break;
            default:
        }
        }
        try {
            ArrayList<Cours> listeCours = (ArrayList<Cours>) coursService.getProchainsCours();

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
        catch (RepositoryException re) {
            // si aucun cours n'a été trouvé, on affiche un message
            model.addAttribute("aucunCours", re.getMessage());
        }
        model.addAttribute(TITRE_PAGE, "Prochains Cours");

        return "prochainsCours";
    }

    //----------------------------------------------------------------------------------------------
    // Méthodes
    //----------------------------------------------------------------------------------------------

    /**
     * Méthode pour compléter un cours d'après le retour de formulaire.
     *
     * @param coursDto       Cours à compléter
     * @param idInstrument   Id de l'instrument du cours
     * @param idEnseignant   Id de l'enseignant du cours
     * @param idParticipants Liste des id des participants du cours
     *
     * @return Cours complété
     *
     * @throws RepositoryException Si une erreur est survenue
     */
    private Cours completerCours(final CoursDto coursDto, final int idInstrument,
                                 final int idEnseignant, final List<Integer> idParticipants)
            throws RepositoryException {

        Cours cours = Cours.clone(coursDto);
        cours.setInstrument(instrumentService.getInstrument(idInstrument));
        cours.setEnseignant(membreService.getMembre(idEnseignant));
        ArrayList<Membre> participants = new ArrayList<>();
        for (int id : idParticipants) {
            Membre membre = membreService.getMembre(id);
            participants.add(membre);
        }
        cours.setParticipants(participants);

        return cours;
    }

    /**
     * Méthode pour récupérer les erreurs du cours avec le validator.
     *
     * @param cours Cours : le cours à valider.
     *
     * @return Set : liste des erreurs de saisies pour le cours.
     */
    private Set<ConstraintViolation<Cours>> getErreurs(
            final Cours cours) {

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {

            Validator validator =
                    validatorFactory.getValidator();

            return validator.validate(cours);
        }
    }

    /**
     * Méthode pour ajouter en attributs les messages d'erreur de saisie.
     *
     * @param erreurs Erreurs de saisie
     * @param model   Modèle de la page
     */
    private void setMessagesErreur(
            final Set<ConstraintViolation<Cours>> erreurs, ModelMap model) {

        for (ConstraintViolation<Cours> erreur : erreurs) {
            model.addAttribute(erreur.getPropertyPath() + "Err", erreur.getMessage());
        }
    }

    /**
     * Méthode pour ajouter les attributs communs aux formulaires.
     *
     * @param model          Modèle de la page
     * @param cours          Cours géré
     * @param idInstrument   Id de l'instrument du cours
     * @param idEnseignant   Id de l'enseignant du cours
     * @param idParticipants Liste des id des participants du cours
     *
     * @throws RepositoryException Si une erreur est survenue
     */
    private void setAttributsCommuns(ModelMap model, Cours cours,
                                     Integer idInstrument, Integer idEnseignant,
                                     List<Integer> idParticipants)
            throws RepositoryException {

        Iterable<Instrument> instruments = instrumentService.getInstruments();
        Iterable<Membre> membres = membreService.getMembres();
        model.addAttribute("instruments", instruments);
        model.addAttribute("membres", membres);
        model.addAttribute("cours", cours);
        model.addAttribute("idInstrument", idInstrument);
        model.addAttribute("idEnseignant", idEnseignant);
        model.addAttribute("idParticipants", idParticipants);
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de création.
     *
     * @param model          Modèle de la page
     * @param cours          Cours géré
     * @param idInstrument   Id de l'instrument du cours
     * @param idEnseignant   Id de l'enseignant du cours
     * @param idParticipants Liste des id des participants du cours
     *
     * @throws RepositoryException Si une erreur est survenue
     */
    private void setAttributsCreation(ModelMap model, Cours cours,
                                      Integer idInstrument, Integer idEnseignant,
                                      List<Integer> idParticipants)
            throws RepositoryException {

        setAttributsCommuns(model, cours, idInstrument, idEnseignant, idParticipants);
        model.addAttribute(ACTION, "/creerCours");
        model.addAttribute(NOM_SUBMIT, "Créer");
        model.addAttribute(TITRE_FORM, "Créer un cours");
        model.addAttribute(TITRE_PAGE, "Créer un cours");
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de modification.
     *
     * @param model          Modèle de la page
     * @param cours          Cours géré
     * @param idInstrument   Id de l'instrument du cours
     * @param idEnseignant   Id de l'enseignant du cours
     * @param idParticipants Liste des id des participants du cours
     * @param idCours        Id du cours
     *
     * @throws RepositoryException Si une erreur est survenue
     */
    private void setAttributsModification(ModelMap model, Cours cours,
                                          Integer idInstrument, Integer idEnseignant,
                                          List<Integer> idParticipants, int idCours)
            throws RepositoryException {

        setAttributsCommuns(model, cours, idInstrument, idEnseignant, idParticipants);
        model.addAttribute(ACTION, "/modifierCours/" + idCours);
        model.addAttribute(NOM_SUBMIT, "Modifier");
        model.addAttribute(TITRE_FORM, "Modifier un cours");
        model.addAttribute(TITRE_PAGE, "Modifier un cours");
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de suppression.
     *
     * @param model          Modèle de la page
     * @param cours          Cours géré
     * @param idInstrument   Id de l'instrument du cours
     * @param idEnseignant   Id de l'enseignant du cours
     * @param idParticipants Liste des id des participants du cours
     * @param idCours        Id du cours
     *
     * @throws RepositoryException Si une erreur est survenue
     */
    private void setAttributsSuppression(ModelMap model, Cours cours,
                                         Integer idInstrument, Integer idEnseignant,
                                         List<Integer> idParticipants, int idCours)
            throws RepositoryException {

        setAttributsCommuns(model, cours, idInstrument, idEnseignant, idParticipants);
        model.addAttribute(ACTION, "/supprimerCours/" + idCours);
        model.addAttribute(NOM_SUBMIT, "Supprimer");
        model.addAttribute(TITRE_FORM, "Supprimer un cours");
        model.addAttribute(TITRE_PAGE, "Supprimer un cours");
    }
}
