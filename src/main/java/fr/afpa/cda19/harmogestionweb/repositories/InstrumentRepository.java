package fr.afpa.cda19.harmogestionweb.repositories;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Instrument;
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

@Slf4j
@EnableConfigurationProperties(CustomProperties.class)
@Component
public class InstrumentRepository {

    //--------------------------------------------------------------------------
    // Attributs
    //--------------------------------------------------------------------------

    /**
     * URL de base de l'API.
     */
    private final String baseApiUrl;

    /**
     * URI concernant les instruments.
     */
    private final String instrumentURI;

    //--------------------------------------------------------------------------
    // Constructeurs
    //--------------------------------------------------------------------------

    /**
     * Constructeur.
     *
     * @param customProperties propriétés de l'API
     */
    public InstrumentRepository(
            @Autowired final CustomProperties customProperties) {
        baseApiUrl = customProperties.getApiUrl();
        instrumentURI = "/instrument";
    }

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Envoi à l'api d'une requête pour récupérer la liste des instruments.
     *
     * @return la liste des instruments, ou null si aucun instrument trouvé.
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Iterable<Instrument> getInstruments() throws RepositoryException {

        String url = baseApiUrl + "/instruments";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<Instrument>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        RepositoryUtil<Iterable<Instrument>> repositoryUtil = new RepositoryUtil<>();
        return repositoryUtil.handleResponse(response);
    }

    /**
     * Envoi à l'api d'une requête pour récupérer l'instrument correspondant à l'id.
     *
     * @param id identifiant de l'instrument recherché
     *
     * @return l'instrument correspondant à l'id
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Instrument getInstrument(final int id) throws RepositoryException {

        String url = baseApiUrl + instrumentURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Instrument> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Instrument.class
        );
        RepositoryUtil<Instrument> repositoryUtil = new RepositoryUtil<>();
        return repositoryUtil.handleResponse(response);
    }

    /**
     * Envoi à l'api d'une requête pour créer un instrument.
     *
     * @param instrument instrument à créer
     *
     * @return l'instrument créé
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Instrument createInstrument(final Instrument instrument)
            throws RepositoryException {

        String url = baseApiUrl + instrumentURI;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Instrument> request = new HttpEntity<>(instrument);
        ResponseEntity<Instrument> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Instrument.class
        );
        RepositoryUtil<Instrument> repositoryUtil = new RepositoryUtil<>();
        return repositoryUtil.handleResponse(response);
    }

    /**
     * Envoi à l'api d'une requête pour modifier un instrument.
     *
     * @param instrument instrument à modifier
     *
     * @return l'instrument modifié
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Instrument updateInstrument(final Instrument instrument)
            throws RepositoryException {

        String url = baseApiUrl + instrumentURI + "/" + instrument.getIdInstrument();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Instrument> request = new HttpEntity<>(instrument);
        ResponseEntity<Instrument> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Instrument.class
        );
        RepositoryUtil<Instrument> repositoryUtil = new RepositoryUtil<>();
        return repositoryUtil.handleResponse(response);
    }

    /**
     * Envoi à l'api d'une requête pour supprimer un instrument.
     *
     * @param id identifiant de l'instrument à supprimer
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public void deleteInstrument(final int id) throws RepositoryException {

        String url = baseApiUrl + instrumentURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        RepositoryUtil<Void> repositoryUtil = new RepositoryUtil<>();
        repositoryUtil.handleResponse(response);
    }
}
