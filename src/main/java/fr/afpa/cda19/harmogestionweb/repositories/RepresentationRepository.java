package fr.afpa.cda19.harmogestionweb.repositories;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Representation;
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
 * Repository des représentations.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 30/04/2026
 */
@Component
@EnableConfigurationProperties(CustomProperties.class)
@Slf4j
public class RepresentationRepository {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

    /**
     * URL de base de l'API.
     */
    private final String baseApiUrl;

    /**
     * URI concernant les représentations.
     */
    private final String representationURI;

    //----------------------------------------------------------------------------------------------
    // Constructeurs
    //----------------------------------------------------------------------------------------------

    /**
     * Constructeur.
     *
     * @param customProperties propriétés de l'API
     */
    public RepresentationRepository(
            @Autowired final CustomProperties customProperties) {

        baseApiUrl = customProperties.getApiUrl();
        representationURI = "/representation";
    }

    //----------------------------------------------------------------------------------------------
    // Méthodes
    //----------------------------------------------------------------------------------------------

    /**
     * Envoi à l'api d'une requête pour récupérer les prochaines représentations.
     *
     * @return la liste des prochaines représentations
     *
     * @throws RepositoryException si aucune représentation trouvée
     */
    public Iterable<Representation> getProchainesRepresentations() throws RepositoryException {

        try {
            String url = baseApiUrl + "/representations";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Iterable<Representation>> response = restTemplate.exchange(
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
     * Envoi à l'api d'une requête pour récupérer la représentation correspondant à l'id.
     *
     * @param id identifiant de la représentation recherchée
     *
     * @return la représentation correspondant à l'id
     *
     */
    public Representation getRepresentation(final int id) {

        String url = baseApiUrl + representationURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Representation> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Representation.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour créer une représentation.
     *
     * @param representation représentation à créer
     *
     * @return la représentation créée
     *
     */
    public Representation createRepresentation(final Representation representation) {

        String url = baseApiUrl + representationURI;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Representation> request = new HttpEntity<>(representation);
        ResponseEntity<Representation> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Representation.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour modifier une représentation.
     *
     * @param representation représentation à modifier
     *
     * @return la représentation modifiée
     *
     */
    public Representation updateRepresentation(final Representation representation) {

        String url = baseApiUrl + representationURI + "/" + representation.getIdRepresentation();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Representation> request = new HttpEntity<>(representation);
        ResponseEntity<Representation> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Representation.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour supprimer une représentation.
     *
     * @param id identifiant de la représentation à supprimer
     *
     */
    public void deleteRepresentation(final int id) {

        String url = baseApiUrl + representationURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
