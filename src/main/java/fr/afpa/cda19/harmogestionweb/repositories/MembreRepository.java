package fr.afpa.cda19.harmogestionweb.repositories;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Membre;
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
 * Repository des membres.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 24/04/2026
 */
@Component
@EnableConfigurationProperties(CustomProperties.class)
@Slf4j
public class MembreRepository {

    //--------------------------------------------------------------------------
    // Attributs
    //--------------------------------------------------------------------------

    /**
     * URL de base de l'API.
     */
    private final String baseApiUrl;

    /**
     * URI concernant les membres.
     */
    private final String membreURI;

    //--------------------------------------------------------------------------
    // Constructeurs
    //--------------------------------------------------------------------------

    /**
     * Constructeur.
     *
     * @param customProperties propriétés de l'API
     */
    public MembreRepository(
            @Autowired final CustomProperties customProperties) {

        baseApiUrl = customProperties.getApiUrl();
        membreURI = "/membre";
    }

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Envoi à l'api d'une requête pour récupérer la liste des membres.
     *
     * @return la liste des membres
     *
     * @throws RepositoryException si aucun membre trouvé
     */
    public Iterable<Membre> getMembres() throws RepositoryException {

        try {
            String url = baseApiUrl + "/membres";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Iterable<Membre>> response = restTemplate.exchange(
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
     * Envoi à l'api d'une requête pour récupérer le membre correspondant à l'id.
     *
     * @param id identifiant du membre recherché
     *
     * @return le membre correspondant à l'id
     *
     */
    public Membre getMembre(final int id) {

        String url = baseApiUrl + membreURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Membre> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Membre.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour créer un membre.
     *
     * @param membre membre à créer
     *
     * @return le membre créé
     *
     */
    public Membre createMembre(final Membre membre) {

        String url = baseApiUrl + membreURI;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Membre> request = new HttpEntity<>(membre);
        ResponseEntity<Membre> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Membre.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour modifier un membre.
     *
     * @param membre membre à modifier
     *
     * @return le membre modifié
     *
     */
    public Membre updateMembre(final Membre membre) {

        String url = baseApiUrl + membreURI + "/" + membre.getIdMembre();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Membre> request = new HttpEntity<>(membre);
        ResponseEntity<Membre> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Membre.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour supprimer un membre.
     *
     * @param id identifiant du membre à supprimer
     *
     * @throws RepositoryException si le membre est utilisé
     */
    public void deleteMembre(final int id) throws RepositoryException {

        try {
            String url = baseApiUrl + membreURI + "/" + id;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );
        }
        catch (HttpClientErrorException hcee) {
            throw new RepositoryException(hcee.getResponseBodyAsString());
        }
    }
}
