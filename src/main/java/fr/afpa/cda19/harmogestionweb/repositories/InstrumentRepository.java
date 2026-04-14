package fr.afpa.cda19.harmogestionweb.repositories;

import fr.afpa.cda19.harmogestionweb.models.Instrument;
import fr.afpa.cda19.harmogestionweb.utilities.CustomProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@EnableConfigurationProperties(CustomProperties.class)
@Component
public class InstrumentRepository {

    /**
     * URL de base de l'API.
     */
    private final String baseApiUrl;

    /**
     * URI des endpoints create, read by id, update, et delete des instruments.
     */
    private final String crudUri;

    public InstrumentRepository(
            @Autowired
            final CustomProperties customProperties) {
        baseApiUrl = customProperties.getApiUrl();
        crudUri = "/instrument";
    }

    public Iterable<Instrument> getInstruments() {
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

    public Instrument getInstrument(final Integer id) {
        String url = baseApiUrl + crudUri + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Instrument> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Instrument.class
        );
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            assert response.getBody() != null;
            log.info(response.getBody().getLibelleInstrument());
            return null;
        }
        return response.getBody();
    }

    public Instrument createInstrument(final Instrument instrument) {
        String url = baseApiUrl + crudUri;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Instrument> request = new HttpEntity<>(instrument);
        ResponseEntity<Instrument> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Instrument.class
        );
        switch (response.getStatusCode()) {
            case HttpStatus.CREATED:
                return response.getBody();
            case HttpStatus.BAD_REQUEST:
                assert response.getBody() != null;
                log.warn(response.getBody().getLibelleInstrument());
                break;
            default:
                assert response.getBody() != null;
                log.error(response.getBody().getLibelleInstrument());
        }
        return null;
    }

    public Instrument updateInstrument(final Instrument instrument) {
        String url = baseApiUrl + crudUri + "/" + instrument.getIdInstrument();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Instrument> request = new HttpEntity<>(instrument);
        ResponseEntity<Instrument> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Instrument.class
        );
        switch (response.getStatusCode()) {
            case HttpStatus.OK:
                return response.getBody();
            case HttpStatus.BAD_REQUEST:
                assert response.getBody() != null;
                log.warn(response.getBody().getLibelleInstrument());
                break;
            default:
                assert response.getBody() != null;
                log.error(response.getBody().getLibelleInstrument());
        }
        return null;
    }

    public void deleteInstrument(final Integer id) {
        String url = baseApiUrl + crudUri + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }
}
