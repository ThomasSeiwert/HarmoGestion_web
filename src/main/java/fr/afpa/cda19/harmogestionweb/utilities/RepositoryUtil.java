package fr.afpa.cda19.harmogestionweb.utilities;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Classe pour la gestion des codes retours de l'API.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 24/04/2026
 */
@Slf4j
public final class RepositoryUtil<T> {

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Méthode pour gérer les codes retour de l'API.
     *
     * @param response Réponse de l'API
     *
     * @return le corps de la requête de l'API
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public T handleResponse(ResponseEntity<T> response) throws RepositoryException {

        switch (response.getStatusCode()) {
            case HttpStatus.OK, HttpStatus.CREATED:
                return response.getBody();
            case HttpStatus.FORBIDDEN, HttpStatus.NO_CONTENT:
                if (response.getBody() != null) {
                    throw new RepositoryException(response.getBody().toString());
                } else {
                    logError(response);
                    throw new RepositoryRuntimeException("Une erreur est survenue");
                }
            default:
                logError(response);
                throw new RepositoryRuntimeException("Une erreur est survenue");
        }
    }

    /**
     * Méthode pour créer un log si une erreur est survenue.
     *
     * @param response réponse de l'API.
     *
     * @throws RepositoryRuntimeException si une erreur est survenue
     */
    private void logError(ResponseEntity<T> response)
            throws RepositoryRuntimeException {

        log.error("Erreur lors de la requête vers l'api - "
                + response);
    }
}
