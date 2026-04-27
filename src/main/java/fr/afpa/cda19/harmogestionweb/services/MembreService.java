package fr.afpa.cda19.harmogestionweb.services;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Membre;
import fr.afpa.cda19.harmogestionweb.repositories.MembreRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service des membres.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 24/04/2026
 */
@Service
@Data
public class MembreService {

    //--------------------------------------------------------------------------
    // Attributs
    //--------------------------------------------------------------------------

    /**
     * Instance de la repository des membres.
     */
    private MembreRepository membreRepository;

    //--------------------------------------------------------------------------
    // Constructeurs
    //--------------------------------------------------------------------------

    /**
     * Constructeur du service des membres.
     *
     * @param membreRepository repository des membres.
     */
    @Autowired
    public MembreService(final MembreRepository membreRepository) {

        this.membreRepository = membreRepository;
    }

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Service pour récupérer la liste des membres.
     *
     * @return la liste des membres, ou null si aucun membre trouvé.
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Iterable<Membre> getMembres() throws RepositoryException {

        return membreRepository.getMembres();
    }

    /**
     * Service pour récupérer le membre correspondant à l'id.
     *
     * @param id identifiant du membre recherché
     *
     * @return le membre correspondant à l'id
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Membre getMembre(final int id) throws RepositoryException {

        return membreRepository.getMembre(id);
    }

    /**
     * Service pour créer ou modifier un membre.
     *
     * @param membre membre à créer ou modifier
     *
     * @return le membre créé ou modifié
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Membre saveMembre(final Membre membre) throws RepositoryException {

        if (membre.getIdMembre() == null) {
            return membreRepository.createMembre(membre);
        } else {
            return membreRepository.updateMembre(membre);
        }
    }

    /**
     * Service pour supprimer un membre.
     *
     * @param id identifiant du membre à supprimer
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public void deleteMembre(final int id) throws RepositoryException {

        membreRepository.deleteMembre(id);
    }
}
