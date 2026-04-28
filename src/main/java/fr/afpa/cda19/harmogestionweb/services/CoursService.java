package fr.afpa.cda19.harmogestionweb.services;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Cours;
import fr.afpa.cda19.harmogestionweb.repositories.CoursRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service pour les cours.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 10/04/2026
 */
@Service
@Data
public class CoursService {

    //--------------------------------------------------------------------------
    // Attributs
    //--------------------------------------------------------------------------

    /**
     * Instance de la repository des cours.
     */
    private CoursRepository coursRepository;

    //--------------------------------------------------------------------------
    // Constructeurs
    //--------------------------------------------------------------------------

    /**
     * Constructeur du service des cours.
     *
     * @param coursRepository CoursRepository : repository des cours.
     */
    @Autowired
    public CoursService(final CoursRepository coursRepository) {

        this.coursRepository = coursRepository;
    }

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Service pour récupérer les prochains cours.
     *
     * @return la liste des prochains cours, ou null si aucun cours trouvé.
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Iterable<Cours> getProchainsCours() throws RepositoryException {

        return coursRepository.getProchainsCours();
    }

    /**
     * Service pour récupérer le cours correspondant à l'id.
     *
     * @param id identifiant du cours recherché
     *
     * @return le cours correspondant à l'id
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Cours getCours(final int id) throws RepositoryException {

        return coursRepository.getCours(id);
    }

    /**
     * Service pour créer ou modifier un cours.
     *
     * @param cours cours à créer ou modifier
     *
     * @return le cours créé ou modifié
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Cours saveCours(final Cours cours) throws RepositoryException {

        if (cours.getIdCours() == null) {
            return coursRepository.createCours(cours);
        } else {
            return coursRepository.updateCours(cours);
        }
    }

    /**
     * Service pour supprimer un cours.
     *
     * @param id identifiant du cours à supprimer
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public void deleteCours(final int id) throws RepositoryException {

        coursRepository.deleteCours(id);
    }
}
