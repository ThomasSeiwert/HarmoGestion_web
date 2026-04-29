package fr.afpa.cda19.harmogestionweb.utilities;

import org.springframework.ui.Model;

/**
 * Utilitaire pour les controllers.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 29/04/2026
 */
public interface ControllerUtil {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

    String STATUT = "statut";

    //----------------------------------------------------------------------------------------------
    // Méthodes
    //----------------------------------------------------------------------------------------------

    /**
     * Méthode pour ajouter un message d'information selon le statut.
     *
     * @param statut String
     * @param model Modèle de la page
     */
    static void setStatus(final String statut, final Model model) {

        switch (statut) {
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
}
