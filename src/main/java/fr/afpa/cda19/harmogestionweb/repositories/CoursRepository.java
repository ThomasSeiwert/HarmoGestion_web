package fr.afpa.cda19.harmogestionweb.repositories;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Cours;
import fr.afpa.cda19.harmogestionweb.utilities.CustomProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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
     * @return la liste des prochains cours
     *
     * @throws RepositoryException si aucun cours trouvé
     */
    public Iterable<Cours> getProchainsCours() throws RepositoryException {

        try {
            String url = baseApiUrl + coursURI;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Iterable<Cours>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody();
        }
        catch (HttpClientErrorException hcee) {
            throw new RepositoryException(hcee.getResponseBodyAsString());
        }
    }

    /**
     * Envoi à l'api d'une requête pour récupérer le cours correspondant à l'id.
     *
     * @param id identifiant du cours recherché
     *
     * @return le cours correspondant à l'id
     *
     */
    public Cours getCours(final int id) {

        String url = baseApiUrl + coursURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Cours> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Cours.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour créer un cours.
     *
     * @param cours cours à créer
     *
     * @return le cours créé
     *
     */
    public Cours createCours(final Cours cours) {

        String url = baseApiUrl + coursURI;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Cours> request = new HttpEntity<>(cours);
        ResponseEntity<Cours> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Cours.class
        );
       return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour modifier un cours.
     *
     * @param cours cours à modifier
     *
     * @return le cours modifié
     *
     */
    public Cours updateCours(final Cours cours) {

        String url = baseApiUrl + coursURI + "/" + cours.getIdCours();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Cours> request = new HttpEntity<>(cours);
        ResponseEntity<Cours> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Cours.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour supprimer un cours.
     *
     * @param id identifiant du cours à supprimer
     *
     */
    public void deleteCours(final int id) {

        String url = baseApiUrl + coursURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
