package fr.afpa.cda19.harmogestionweb.repositories;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Cours;
import fr.afpa.cda19.harmogestionweb.utilities.CustomProperties;
import fr.afpa.cda19.harmogestionweb.utilities.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Repository des cours.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 10/04/2026
 */
@Component
@EnableConfigurationProperties(CustomProperties.class)
@Slf4j
public class CoursRepository {

    //--------------------------------------------------------------------------
    // Attributs
    //--------------------------------------------------------------------------

    /**
     * URL de base de l'API.
     */
    private final String baseApiUrl;

    /**
     * URI concernant les cours.
     */
    private final String coursURI;

    //--------------------------------------------------------------------------
    // Constructeurs
    //--------------------------------------------------------------------------

    /**
     * Constructeur.
     *
     * @param customProperties propriétés de l'API
     */
    public CoursRepository(
            @Autowired final CustomProperties customProperties) {
        baseApiUrl = customProperties.getApiUrl();
        coursURI = "/cours";
    }

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Envoi à l'api d'une requête pour récupérer les prochains cours.
     *
     * @return la liste des prochains cours, ou null si aucun cours trouvé.
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Iterable<Cours> getProchainsCours() throws RepositoryException {

        String url = baseApiUrl + coursURI;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<Cours>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        RepositoryUtil<Iterable<Cours>> repositoryUtil = new RepositoryUtil<>();
        return repositoryUtil.handleResponse(response,
                "Aucun cours prévu pour le moment");
    }

    /**
     * Envoi à l'api d'une requête pour récupérer le cours correspondant à l'id.
     *
     * @param id identifiant du cours recherché
     *
     * @return le cours correspondant à l'id
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Cours getCours(final int id) throws RepositoryException {

        String url = baseApiUrl + coursURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Cours> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Cours.class
        );
        RepositoryUtil<Cours> repositoryUtil = new RepositoryUtil<>();
        return repositoryUtil.handleResponse(response, null);
    }

    /**
     * Envoi à l'api d'une requête pour créer un cours.
     *
     * @param cours cours à créer
     *
     * @return le cours créé
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Cours createCours(final Cours cours) throws RepositoryException {

        String url = baseApiUrl + coursURI;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Cours> request = new HttpEntity<>(cours);
        ResponseEntity<Cours> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Cours.class
        );
        RepositoryUtil<Cours> repositoryUtil = new RepositoryUtil<>();
        return repositoryUtil.handleResponse(response, null);
    }

    /**
     * Envoi à l'api d'une requête pour modifier un cours.
     *
     * @param cours cours à modifier
     *
     * @return le cours modifié
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Cours updateCours(final Cours cours) throws RepositoryException {

        String url = baseApiUrl + coursURI + "/" + cours.getIdCours();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Cours> request = new HttpEntity<>(cours);
        ResponseEntity<Cours> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Cours.class
        );
        RepositoryUtil<Cours> repositoryUtil = new RepositoryUtil<>();
        return repositoryUtil.handleResponse(response, null);
    }

    /**
     * Envoi à l'api d'une requête pour supprimer un cours.
     *
     * @param id identifiant du cours à supprimer
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public void deleteCours(final int id) throws RepositoryException {

        String url = baseApiUrl + coursURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        RepositoryUtil<Void> repositoryUtil = new RepositoryUtil<>();
        repositoryUtil.handleResponse(response, null);
    }
}
