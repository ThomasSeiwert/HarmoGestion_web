package fr.afpa.cda19.harmogestionweb.repositories;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Instrument;
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
     * @return la liste des instruments, ou null si aucun instrument trouvé
     *
     * @throws RepositoryException si aucun instrument trouvé
     */
    public Iterable<Instrument> getInstruments() throws RepositoryException {

        try {
            String url = baseApiUrl + "/instruments";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Iterable<Instrument>> response = restTemplate.exchange(
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
     * Envoi à l'api d'une requête pour récupérer l'instrument correspondant à l'id.
     *
     * @param id identifiant de l'instrument recherché
     *
     * @return l'instrument correspondant à l'id
     *
     */
    public Instrument getInstrument(final int id) {

        String url = baseApiUrl + instrumentURI + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Instrument> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Instrument.class
        );
        return response.getBody();
    }

    /**
     * Envoi à l'api d'une requête pour créer un instrument.
     *
     * @param instrument instrument à créer
     *
     * @return l'instrument créé
     *
     * @throws RepositoryException si le libellé de l'instrument existe déjà
     */
    public Instrument createInstrument(final Instrument instrument) throws RepositoryException {

        try {
            String url = baseApiUrl + instrumentURI;
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Instrument> request = new HttpEntity<>(instrument);
            ResponseEntity<Instrument> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Instrument.class
            );
            return response.getBody();
        }
        catch (HttpClientErrorException hcee) {
            throw new RepositoryException(hcee.getResponseBodyAsString());
        }
    }

    /**
     * Envoi à l'api d'une requête pour modifier un instrument.
     *
     * @param instrument instrument à modifier
     *
     * @return l'instrument modifié
     *
     * @throws RepositoryException si le libellé de l'instrument existe déjà
     */
    public Instrument updateInstrument(final Instrument instrument) throws RepositoryException {

        try {
            String url = baseApiUrl + instrumentURI + "/" + instrument.getIdInstrument();
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Instrument> request = new HttpEntity<>(instrument);
            ResponseEntity<Instrument> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    request,
                    Instrument.class
            );
            return response.getBody();
        }
        catch (HttpClientErrorException hcee) {
            throw new RepositoryException(hcee.getResponseBodyAsString());
        }
    }

    /**
     * Envoi à l'api d'une requête pour supprimer un instrument.
     *
     * @param id identifiant de l'instrument à supprimer
     *
     * @throws RepositoryException si l'instrument est encore utilisé
     */
    public void deleteInstrument(final int id) throws RepositoryException {

        try {
            String url = baseApiUrl + instrumentURI + "/" + id;
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
