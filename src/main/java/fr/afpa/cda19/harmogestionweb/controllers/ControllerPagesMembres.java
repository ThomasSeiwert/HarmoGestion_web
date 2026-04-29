package fr.afpa.cda19.harmogestionweb.controllers;

import fr.afpa.cda19.harmogestionweb.dto.MembreDto;
import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Instrument;
import fr.afpa.cda19.harmogestionweb.models.Membre;
import fr.afpa.cda19.harmogestionweb.services.InstrumentService;
import fr.afpa.cda19.harmogestionweb.services.MembreService;
import fr.afpa.cda19.harmogestionweb.utilities.ControllerUtil;
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
public class ControllerPagesMembres {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

    /**
     * Instance du service des membres.
     */
    private final MembreService membreService;

    /**
     * Instance du service des instruments.
     */
    private final InstrumentService instrumentService;

    private static final String ACTION = "action";
    private static final String NOM_SUBMIT = "nomSubmit";
    private static final String TITRE_FORM = "titreFormulaire";
    private static final String TITRE_PAGE = "titrePage";
    private static final String STATUT = "statut";
    private static final String FORM_MEMBRE = "formMembre";
    private static final String URL_REDIRECT = "redirect:/listeMembres";

    //----------------------------------------------------------------------------------------------
    // Constructeurs
    //----------------------------------------------------------------------------------------------

    /**
     * Constructeur du controller des pages des membres.
     *
     * @param membreService service des membres.
     */
    @Autowired
    public ControllerPagesMembres(final MembreService membreService,
                                  final InstrumentService instrumentService) {

        this.instrumentService = instrumentService;
        this.membreService = membreService;
    }

    //----------------------------------------------------------------------------------------------
    // Endpoints
    //----------------------------------------------------------------------------------------------

    /**
     * Méthode d'accès à la page de création de membre vierge.
     *
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/creerMembre")
    public String creerMembreGet(ModelMap model) {

        setAttributsCreation(model, new Membre(), new ArrayList<>(), new ArrayList<>());

        return FORM_MEMBRE;
    }

    /**
     * Méthode d'accès à la page de création de membre pour un retour de formulaire.
     *
     * @param idInsMaitrises Liste des id des instruments maitrisés
     * @param idInsAppris    Liste des id des instruments appris
     * @param membreDto      Membre : membre entré dans le formulaire
     * @param model          Modèle de la page
     *
     * @return page de formulaire si erreur, redirect à la liste des membres si succès
     */
    @PostMapping("/creerMembre")
    public ModelAndView creerMembrePost(
            @RequestParam(value = "idInsMaitrises", required = false) List<Integer> idInsMaitrises,
            @RequestParam(value = "idInsAppris", required = false) List<Integer> idInsAppris,
            @ModelAttribute final MembreDto membreDto,
            ModelMap model) {

        if (idInsMaitrises == null) {
            idInsMaitrises = new ArrayList<>();
        }
        if (idInsAppris == null) {
            idInsAppris = new ArrayList<>();
        }
        Membre membre = completerMembre(membreDto, idInsMaitrises, idInsAppris);

        Set<ConstraintViolation<Membre>> erreurs = getErreurs(membre);
        setMessagesErreur(erreurs, model);

        if (!erreurs.isEmpty()) {
            setAttributsCreation(model, membre, idInsMaitrises, idInsAppris);

            return new ModelAndView(FORM_MEMBRE, model);
        }
        else {
            membreService.saveMembre(membre);
            model.addAttribute(STATUT, "created");

            return new ModelAndView(URL_REDIRECT, model);
        }
    }

    /**
     * Méthode d'accès à la page de modification d'un membre (1er envoi).
     *
     * @param id    Identifiant du membre à modifier
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/modifierMembre/{id}")
    public String modifierMembreGet(
            @PathVariable final int id,
            ModelMap model) {

        Membre membre = membreService.getMembre(id);
        setAttributsModification(model, membre, getIdInsMaitrises(membre),
                getIdInsAppris(membre), id);

        return FORM_MEMBRE;
    }

    /**
     * Méthode d'accès à la page de modification de membre pour retour de formulaire.
     *
     * @param id             int : id du membre
     * @param idInsMaitrises Liste des id des instruments maitrisés
     * @param idInsAppris    Liste des id des instruments appris
     * @param membreDto      Membre : le membre
     * @param model          Modèle de la page
     *
     * @return page de formulaire si erreur, redirect à la liste des membres si succès
     */
    @PostMapping("/modifierMembre/{id}")
    public ModelAndView modifierMembrePost(
            @PathVariable final int id,
            @RequestParam(value = "idInsMaitrises", required = false) List<Integer> idInsMaitrises,
            @RequestParam(value = "idInsAppris", required = false) List<Integer> idInsAppris,
            @ModelAttribute final MembreDto membreDto,
            ModelMap model) {

        if (idInsMaitrises == null) {
            idInsMaitrises = new ArrayList<>();
        }
        if (idInsAppris == null) {
            idInsAppris = new ArrayList<>();
        }
        Membre membre = completerMembre(membreDto, idInsMaitrises, idInsAppris);

        Set<ConstraintViolation<Membre>> erreurs = getErreurs(membre);
        setMessagesErreur(erreurs, model);

        if (!erreurs.isEmpty()) {
            setAttributsModification(model, membre, idInsMaitrises, idInsAppris, id);

            return new ModelAndView(FORM_MEMBRE, model);
        }
        else {
            membreService.saveMembre(membre);
            model.addAttribute(STATUT, "updated");

            return new ModelAndView(URL_REDIRECT, model);
        }
    }

    /**
     * Méthode d'accès à la page de suppression d'un membre (1er envoi).
     *
     * @param id    Identifiant du membre à supprimer
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/supprimerMembre/{id}")
    public String supprimerMembresGet(
            @PathVariable final int id,
            ModelMap model) {

        Membre membre = membreService.getMembre(id);
        setAttributsSuppression(model, membre, getIdInsMaitrises(membre),
                getIdInsAppris(membre), id);

        return FORM_MEMBRE;
    }

    /**
     * Méthode d'accès à la page de suppression de membre pour retour de formulaire.
     *
     * @param id    int : id du membre
     * @param model Modèle de la page
     *
     * @return redirect à la liste des membres
     */
    @PostMapping("/supprimerMembre/{id}")
    public ModelAndView supprimerMembrePost(
            @PathVariable final int id,
            ModelMap model) {

        try {
            membreService.deleteMembre(id);
            model.addAttribute(STATUT, "deleted");

            return new ModelAndView(URL_REDIRECT, model);
        }
        catch (RepositoryException re) {
            Membre membre = membreService.getMembre(id);
            setAttributsSuppression(model, membre, getIdInsMaitrises(membre),
                    getIdInsAppris(membre), id);
            model.addAttribute("alert", re.getMessage());

            return new ModelAndView(FORM_MEMBRE, model);
        }
    }

    /**
     * Méthode d'accès à la page de la liste des membres.
     *
     * @param model Modèle de la page.
     *
     * @return URI de la page.
     */
    @GetMapping("/listeMembres")
    public String listeMembres(
            @RequestParam(required = false) Optional<String> statut,
            Model model) {

        statut.ifPresent(string -> ControllerUtil.setStatus(string, model));
        try {
            ArrayList<Membre> listeMembres = (ArrayList<Membre>) membreService.getMembres();

            // si un membre a été trouvé, on affiche la liste des membres
            listeMembres.sort(Membre.COMPARATOR_NOM);
            model.addAttribute("membres", listeMembres);
        }
        catch (RepositoryException re) {
            // si aucun membre n'a été trouvé, on affiche un message
            model.addAttribute("aucunMembre", re.getMessage());
        }
        model.addAttribute(TITRE_PAGE, "Liste membres");

        return "listeMembres";
    }

//----------------------------------------------------------------------------------------------
// Méthodes
//----------------------------------------------------------------------------------------------

    /**
     * Méthode pour compléter un membre d'après le retour de formulaire.
     *
     * @param membreDto              Membre à compléter
     * @param idInstrumentsMaitrises Liste des id des instruments maitrisés
     * @param idInstrumentsAppris    Liste des id des instruments appris
     *
     * @return Membre complété
     *
     */
    private Membre completerMembre(final MembreDto membreDto, final List<Integer> idInstrumentsMaitrises,
                                   final List<Integer> idInstrumentsAppris) {

        Membre membre = Membre.clone(membreDto);
        ArrayList<Instrument> instrumentsMaitrises = new ArrayList<>();
        ArrayList<Instrument> instrumentsAppris = new ArrayList<>();
        for (int id : idInstrumentsMaitrises) {
            Instrument instrument = instrumentService.getInstrument(id);
            instrumentsMaitrises.add(instrument);
        }
        membre.setInstrumentsMaitrises(instrumentsMaitrises);
        for (int id : idInstrumentsAppris) {
            Instrument instrument = instrumentService.getInstrument(id);
            instrumentsAppris.add(instrument);
        }
        membre.setInstrumentsAppris(instrumentsAppris);

        return membre;
    }

    /**
     * Méthode pour récupérer les erreurs du membre avec le validator.
     *
     * @param membre Membre : le membre à valider.
     *
     * @return Set : liste des erreurs de saisies pour le membre.
     */
    private Set<ConstraintViolation<Membre>> getErreurs(
            final Membre membre) {

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {

            Validator validator =
                    validatorFactory.getValidator();

            return validator.validate(membre);
        }
    }

    /**
     * Méthode pour ajouter en attributs les messages d'erreur de saisie.
     *
     * @param erreurs Erreurs de saisie
     * @param model   Modèle de la page
     */
    private void setMessagesErreur(
            final Set<ConstraintViolation<Membre>> erreurs, ModelMap model) {

        for (ConstraintViolation<Membre> erreur : erreurs) {
            model.addAttribute(erreur.getPropertyPath() + "Err", erreur.getMessage());
        }
    }

    private List<Integer> getIdInsMaitrises(final Membre membre) {

        ArrayList<Integer> idInsMaitrises = new ArrayList<>();
        for (Instrument instrument : membre.getInstrumentsMaitrises()) {
            idInsMaitrises.add(instrument.getIdInstrument());
        }
        return idInsMaitrises;
    }

    private List<Integer> getIdInsAppris(final Membre membre) {

        ArrayList<Integer> idInsAppris = new ArrayList<>();
        for (Instrument instrument : membre.getInstrumentsAppris()) {
            idInsAppris.add(instrument.getIdInstrument());
        }
        return idInsAppris;
    }

    /**
     * Méthode pour ajouter les attributs communs aux formulaires.
     *
     * @param model          Modèle de la page
     * @param membre         Membre géré
     * @param idInsMaitrises Liste des id des instruments maitrisés
     * @param idInsAppris    Liste des id des instruments appris
     *
     */
    private void setAttributsCommuns(ModelMap model, Membre membre, List<Integer> idInsMaitrises,
                                     List<Integer> idInsAppris) {

        Iterable<Instrument> instruments = null;
        try {
            instruments = instrumentService.getInstruments();
        }
        catch (RepositoryException _) {
            instruments = new ArrayList<>();
        }
        finally {
            model.addAttribute("instruments", instruments);
            model.addAttribute("membre", membre);
            model.addAttribute("idInsMaitrises", idInsMaitrises);
            model.addAttribute("idInsAppris", idInsAppris);
        }
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de création.
     *
     * @param model          Modèle de la page
     * @param membre         Membre géré
     * @param idInsMaitrises Liste des id des instruments maitrisés
     * @param idInsAppris    Liste des id des instruments appris
     *
     */
    private void setAttributsCreation(ModelMap model, Membre membre, List<Integer> idInsMaitrises,
                                      List<Integer> idInsAppris) {

        setAttributsCommuns(model, membre, idInsMaitrises, idInsAppris);
        model.addAttribute(ACTION, "/creerMembre");
        model.addAttribute(NOM_SUBMIT, "Créer");
        model.addAttribute(TITRE_FORM, "Créer un membre");
        model.addAttribute(TITRE_PAGE, "Créer un membre");
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de modification.
     *
     * @param model          Modèle de la page
     * @param membre         Membre géré
     * @param idInsMaitrises Liste des id des instruments maitrisés
     * @param idInsAppris    Liste des id des instruments appris
     * @param idMembre       Id du membre
     *
     */
    private void setAttributsModification(ModelMap model, Membre membre, List<Integer> idInsMaitrises,
                                          List<Integer> idInsAppris, int idMembre) {

        setAttributsCommuns(model, membre, idInsMaitrises, idInsAppris);
        model.addAttribute(ACTION, "/modifierMembre/" + idMembre);
        model.addAttribute(NOM_SUBMIT, "Modifier");
        model.addAttribute(TITRE_FORM, "Modifier un membre");
        model.addAttribute(TITRE_PAGE, "Modifier un membre");
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de suppression.
     *
     * @param model          Modèle de la page
     * @param membre         Membre géré
     * @param idInsMaitrises Liste des id des instruments maitrisés
     * @param idInsAppris    Liste des id des instruments appris
     * @param idMembre       Id du membre
     *
     */
    private void setAttributsSuppression(ModelMap model, Membre membre, List<Integer> idInsMaitrises,
                                         List<Integer> idInsAppris, int idMembre) {

        setAttributsCommuns(model, membre, idInsMaitrises, idInsAppris);
        model.addAttribute(ACTION, "/supprimerMembre/" + idMembre);
        model.addAttribute(NOM_SUBMIT, "Supprimer");
        model.addAttribute(TITRE_FORM, "Supprimer un membre");
        model.addAttribute(TITRE_PAGE, "Supprimer un membre");
    }
}
