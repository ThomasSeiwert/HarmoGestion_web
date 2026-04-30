package fr.afpa.cda19.harmogestionweb.services;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Representation;
import fr.afpa.cda19.harmogestionweb.repositories.RepresentationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service des représentations.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 30/04/2026
 */
@Service
@Data
public class RepresentationService {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

    /**
     * Instance de la repository des représentations.
     */
    private RepresentationRepository representationRepository;

    //----------------------------------------------------------------------------------------------
    // Constructeurs
    //----------------------------------------------------------------------------------------------

    /**
     * Constructeur du service des représentations.
     *
     * @param representationRepository repository des représentations.
     */
    @Autowired
    public RepresentationService(final RepresentationRepository representationRepository) {

        this.representationRepository = representationRepository;
    }

    //----------------------------------------------------------------------------------------------
    // Méthodes
    //----------------------------------------------------------------------------------------------

    /**
     * Service pour récupérer les prochaines représentations.
     *
     * @return la liste des prochaines représentations
     *
     * @throws RepositoryException si aucune représentation trouvée
     */
    public Iterable<Representation> getProchainesRepresentations() throws RepositoryException {

        return representationRepository.getProchainesRepresentations();
    }

    /**
     * Service pour récupérer la représentation correspondant à l'id.
     *
     * @param id identifiant de la représentation recherchée
     *
     * @return la représentation correspondant à l'id
     *
     */
    public Representation getRepresentation(final int id) {

        return representationRepository.getRepresentation(id);
    }

    /**
     * Service pour créer ou modifier une representation.
     *
     * @param representation représentation à créer ou modifier
     *
     * @return la représentation créée ou modifiée
     *
     */
    public Representation saveRepresentation(final Representation representation) {

        if (representation.getIdRepresentation() == null) {
            return representationRepository.createRepresentation(representation);
        }
        else {
            return representationRepository.updateRepresentation(representation);
        }
    }

    /**
     * Service pour supprimer une représentation.
     *
     * @param id identifiant de la représentation à supprimer
     *
     */
    public void deleteRepresentation(final int id) {

        representationRepository.deleteRepresentation(id);
    }
}
