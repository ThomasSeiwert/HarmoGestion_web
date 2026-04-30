package fr.afpa.cda19.harmogestionweb.controllers;

import fr.afpa.cda19.harmogestionweb.dto.RepresentationDto;
import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Instrument;
import fr.afpa.cda19.harmogestionweb.models.Membre;
import fr.afpa.cda19.harmogestionweb.models.Representation;
import fr.afpa.cda19.harmogestionweb.services.InstrumentService;
import fr.afpa.cda19.harmogestionweb.services.MembreService;
import fr.afpa.cda19.harmogestionweb.services.RepresentationService;
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
 * Classe de controller liée aux représentations.
 */
@Controller
public class ControllerRepresentation {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

    /**
     * Instance du service des représentations.
     */
    private final RepresentationService representationService;

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
    private static final String FORM_REPRESENTATION = "formRepresentation";
    private static final String URL_REDIRECT = "redirect:/prochainesRepresentations";

    //----------------------------------------------------------------------------------------------
    // Constructeurs
    //----------------------------------------------------------------------------------------------

    /**
     * Constructeur du controller des pages des représentations.
     *
     * @param representationService service des représentations
     * @param instrumentService     service des instruments
     * @param membreService         service des membres
     */
    @Autowired
    public ControllerRepresentation(final RepresentationService representationService,
                                    final InstrumentService instrumentService,
                                    final MembreService membreService) {

        this.representationService = representationService;
        this.instrumentService = instrumentService;
        this.membreService = membreService;
    }

    //----------------------------------------------------------------------------------------------
    // Endpoints
    //----------------------------------------------------------------------------------------------

    /**
     * Méthode d'accès à la page de création de représentation vierge.
     *
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/creerRepresentation")
    public String creerRepresentationGet(ModelMap model) {

        setAttributsCreation(model, new Representation(), new ArrayList<>(), new ArrayList<>());

        return FORM_REPRESENTATION;
    }

    /**
     * Méthode d'accès à la page de création d'une représentation pour un retour de formulaire.
     *
     * @param idParticipants    Liste des id des participants
     * @param idInstruments     Liste des id des instruments
     * @param representationDto Représentation entrée dans le formulaire
     * @param model             Modèle de la page
     *
     * @return page de formulaire si erreur, redirect aux prochaines représentations si succès
     */
    @PostMapping("/creerRepresentation")
    public ModelAndView creerRepresentationPost(
            @RequestParam(value = "idParticipants", required = false) List<Integer> idParticipants,
            @RequestParam(value = "idInstruments", required = false) List<Integer> idInstruments,
            @ModelAttribute final RepresentationDto representationDto,
            ModelMap model) {

        Representation representation = completerRepresentation(representationDto,
                idParticipants, idInstruments);

        Set<ConstraintViolation<Representation>> erreurs = getErreurs(representation);
        setMessagesErreur(erreurs, model);

        if (!erreurs.isEmpty()) {
            setAttributsCreation(model, representation, idParticipants, idInstruments);

            return new ModelAndView(FORM_REPRESENTATION, model);
        }
        else {
            representationService.saveRepresentation(representation);
            model.addAttribute(STATUT, "created");

            return new ModelAndView(URL_REDIRECT, model);
        }
    }

    /**
     * Méthode d'accès à la page de modification d'une représentation (1er envoi).
     *
     * @param id    Identifiant de la représentation à modifier
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/modifierRepresentation/{id}")
    public String modifierRepresentationGet(
            @PathVariable final int id,
            ModelMap model) {

        Representation representation = representationService.getRepresentation(id);
        setAttributsModification(model, representation, getIdParticipants(representation),
                getIdInstruments(representation), id);

        return FORM_REPRESENTATION;
    }

    /**
     * Méthode d'accès à la page de modification d'une représentation pour retour de formulaire.
     *
     * @param id                int : id de la représentation
     * @param idParticipants    Liste des id des participants
     * @param idInstruments     Liste des id des instruments
     * @param representationDto Représentation entrée dans le formulaire
     * @param model             Modèle de la page
     *
     * @return page de formulaire si erreur, redirect aux prochaines représentations si succès
     */
    @PostMapping("/modifierRepresentation/{id}")
    public ModelAndView modifierRepresentationPost(
            @PathVariable final int id,
            @RequestParam(value = "idParticipants", required = false) List<Integer> idParticipants,
            @RequestParam(value = "idInstruments", required = false) List<Integer> idInstruments,
            @ModelAttribute final RepresentationDto representationDto,
            ModelMap model) {


        Representation representation = completerRepresentation(representationDto,
                idParticipants, idInstruments);

        Set<ConstraintViolation<Representation>> erreurs = getErreurs(representation);
        setMessagesErreur(erreurs, model);

        if (!erreurs.isEmpty()) {
            setAttributsModification(model, representation, idParticipants, idInstruments, id);

            return new ModelAndView(FORM_REPRESENTATION, model);
        }
        else {
            representationService.saveRepresentation(representation);
            model.addAttribute(STATUT, "updated");

            return new ModelAndView(URL_REDIRECT, model);
        }
    }

    /**
     * Méthode d'accès à la page de suppression d'une représentation (1er envoi).
     *
     * @param id    Identifiant de la représentation à supprimer
     * @param model Modèle de la page
     *
     * @return URI de la page
     */
    @GetMapping("/supprimerRepresentation/{id}")
    public String supprimerMembresGet(
            @PathVariable final int id,
            ModelMap model) {

        Representation representation = representationService.getRepresentation(id);
        setAttributsSuppression(model, representation, getIdParticipants(representation),
                getIdInstruments(representation), id);

        return FORM_REPRESENTATION;
    }

    /**
     * Méthode d'accès à la page de suppression d'une représentation pour retour de formulaire.
     *
     * @param id    int : id de la représentation
     * @param model Modèle de la page
     *
     * @return redirect aux prochaines représentations
     */
    @PostMapping("/supprimerRepresentation/{id}")
    public ModelAndView supprimerRepresentationPost(
            @PathVariable final int id,
            ModelMap model) {

        representationService.deleteRepresentation(id);
        model.addAttribute(STATUT, "deleted");

        return new ModelAndView(URL_REDIRECT, model);
    }

    /**
     * Méthode d'accès à la page des prochaines repésentations.
     *
     * @param model Modèle de la page.
     *
     * @return URI de la page.
     */
    @GetMapping("/prochainesRepresentations")
    public String prochainesRepresentations(
            @RequestParam(required = false) Optional<String> statut,
            Model model) {

        statut.ifPresent(string -> ControllerUtil.setStatus(string, model));
        try {
            ArrayList<Representation> representations =
                    (ArrayList<Representation>) representationService.getProchainesRepresentations();

            // si une représentation a été trouvée, on affiche les 3 prochaines représentations au max
            representations.sort(Representation.COMPARATOR_DATE);
            ArrayList<Representation> prochainesRepresentations = new ArrayList<>();
            int i = 0;
            while (prochainesRepresentations.size() <= 3 && i < representations.size()) {
                Representation representation = representations.get(i);
                prochainesRepresentations.add(representation);
                i++;
            }
            model.addAttribute("prochainesRepresentations", prochainesRepresentations);
        }
        catch (RepositoryException re) {
            // si aucune représentation n'a été trouvée, on affiche un message
            model.addAttribute("aucuneRepresentation", re.getMessage());
        }
        model.addAttribute(TITRE_PAGE, "Prochaines Representations");

        return "prochainesRepresentations";
    }

    //----------------------------------------------------------------------------------------------
    // Méthodes
    //----------------------------------------------------------------------------------------------

    /**
     * Méthode pour compléter une représentation d'après le retour de formulaire.
     *
     * @param representationDto Représentation à compléter
     * @param idParticipants    Liste des id des participants à la représentation
     * @param idInstruments     Liste des ids des instruments joués à la représentation
     *
     * @return Représentation complétée
     *
     */
    private Representation completerRepresentation(final RepresentationDto representationDto,
                                                   List<Integer> idParticipants,
                                                   List<Integer> idInstruments) {

        Representation representation = Representation.clone(representationDto);
        ArrayList<Membre> participants = new ArrayList<>();
        ArrayList<Instrument> instruments = new ArrayList<>();
        if (idParticipants == null) {
            idParticipants = new ArrayList<>();
        }
        if (idInstruments == null) {
            idInstruments = new ArrayList<>();
        }
        for (int id : idParticipants) {
            Membre membre = membreService.getMembre(id);
            participants.add(membre);
        }
        representation.setParticipants(participants);
        for (int id : idInstruments) {
            Instrument instrument = instrumentService.getInstrument(id);
            instruments.add(instrument);
        }
        representation.setInstruments(instruments);

        return representation;
    }

    /**
     * Méthode pour récupérer les erreurs de la représentation avec le validator.
     *
     * @param representation la représentation à valider.
     *
     * @return Set : liste des erreurs de saisies pour la représentation.
     */
    private Set<ConstraintViolation<Representation>> getErreurs(final Representation representation) {

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {

            Validator validator =
                    validatorFactory.getValidator();

            return validator.validate(representation);
        }
    }

    /**
     * Méthode pour ajouter en attributs les messages d'erreur de saisie.
     *
     * @param erreurs Erreurs de saisie
     * @param model   Modèle de la page
     */
    private void setMessagesErreur(
            final Set<ConstraintViolation<Representation>> erreurs, ModelMap model) {

        for (ConstraintViolation<Representation> erreur : erreurs) {
            model.addAttribute(erreur.getPropertyPath() + "Err", erreur.getMessage());
        }
    }

    private List<Integer> getIdParticipants(final Representation representation) {

        ArrayList<Integer> idParticipants = new ArrayList<>();
        for (Membre membre : representation.getParticipants()) {
            idParticipants.add(membre.getIdMembre());
        }
        return idParticipants;
    }

    private List<Integer> getIdInstruments(final Representation representation) {

        ArrayList<Integer> idInstruments = new ArrayList<>();
        for (Instrument instrument : representation.getInstruments()) {
            idInstruments.add(instrument.getIdInstrument());
        }
        return idInstruments;
    }

    /**
     * Méthode pour ajouter les attributs communs aux formulaires.
     *
     * @param model          Modèle de la page
     * @param representation Représentation gérée
     * @param idParticipants Liste des id des participants de la représentation
     * @param idInstruments  Liste des id des instruments joués à la représentation
     *
     */
    private void setAttributsCommuns(ModelMap model, Representation representation,
                                     List<Integer> idParticipants, List<Integer> idInstruments) {

        Iterable<Membre> membres = null;
        Iterable<Instrument> instruments = null;
        try {
            membres = membreService.getMembres();
            instruments = instrumentService.getInstruments();
        }
        catch (RepositoryException _) {
            membres = new ArrayList<>();
            instruments = new ArrayList<>();
        }
        finally {
            model.addAttribute("instruments", instruments);
            model.addAttribute("membres", membres);
            model.addAttribute("representation", representation);
            model.addAttribute("idParticipants", idParticipants);
            model.addAttribute("idInstruments", idInstruments);
        }
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de création.
     *
     * @param model          Modèle de la page
     * @param representation Représentation gérée
     * @param idParticipants Liste des id des participants de la représentation
     * @param idInstruments  Liste des id des instruments joués à la représentation
     *
     */
    private void setAttributsCreation(ModelMap model, Representation representation,
                                      List<Integer> idParticipants, List<Integer> idInstruments) {

        setAttributsCommuns(model, representation, idParticipants, idInstruments);
        model.addAttribute(ACTION, "/creerRepresentation");
        model.addAttribute(NOM_SUBMIT, "Créer");
        model.addAttribute(TITRE_FORM, "Créer une représentation");
        model.addAttribute(TITRE_PAGE, "Créer une représentation");
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de modification.
     *
     * @param model            Modèle de la page
     * @param representation   Représentation gérée
     * @param idParticipants   Liste des id des participants de la représentation
     * @param idInstruments    Liste des id des instruments joués à la représentation
     * @param idRepresentation Id de la représentation
     *
     */
    private void setAttributsModification(ModelMap model, Representation representation,
                                          List<Integer> idParticipants, List<Integer> idInstruments,
                                          int idRepresentation) {

        setAttributsCommuns(model, representation, idParticipants, idInstruments);
        model.addAttribute(ACTION, "/modifierRepresentation/" + idRepresentation);
        model.addAttribute(NOM_SUBMIT, "Modifier");
        model.addAttribute(TITRE_FORM, "Modifier une représentation");
        model.addAttribute(TITRE_PAGE, "Modifier une représentation");
    }

    /**
     * Méthode pour ajouter les attributs du formulaire de suppression.
     *
     * @param model            Modèle de la page
     * @param representation   Représentation gérée
     * @param idParticipants   Liste des id des participants de la représentation
     * @param idInstruments    Liste des id des instruments joués à la représentation
     * @param idRepresentation Id de la représentation
     *
     */
    private void setAttributsSuppression(ModelMap model, Representation representation,
                                         List<Integer> idParticipants, List<Integer> idInstruments,
                                         int idRepresentation) {

        setAttributsCommuns(model, representation, idParticipants, idInstruments);
        model.addAttribute(ACTION, "/supprimerRepresentation/" + idRepresentation);
        model.addAttribute(NOM_SUBMIT, "Supprimer");
        model.addAttribute(TITRE_FORM, "Supprimer une représentation");
        model.addAttribute(TITRE_PAGE, "Supprimer une représentation");
    }
}
