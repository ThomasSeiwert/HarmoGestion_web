package fr.afpa.cda19.harmogestionweb.services;

import fr.afpa.cda19.harmogestionweb.exceptions.RepositoryException;
import fr.afpa.cda19.harmogestionweb.models.Instrument;
import fr.afpa.cda19.harmogestionweb.repositories.InstrumentRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service pour les instruments.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 10/04/2026
 */
@Service
@Data
public class InstrumentService {

    //--------------------------------------------------------------------------
    // Attributs
    //--------------------------------------------------------------------------

    /**
     * Instance de la repository des instruments.
     */
    private InstrumentRepository instrumentRepository;

    //--------------------------------------------------------------------------
    // Constructeurs
    //--------------------------------------------------------------------------

    /**
     * Constructeur du service des instruments.
     *
     * @param instrumentRepository repository des instruments.
     */
    @Autowired
    public InstrumentService(final InstrumentRepository instrumentRepository) {

        this.instrumentRepository = instrumentRepository;
    }

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Service pour récupérer la liste des instruments.
     *
     * @return la liste des instruments, ou null si aucun instruments trouvé.
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Iterable<Instrument> getInstruments() throws RepositoryException {

        return instrumentRepository.getInstruments();
    }

    /**
     * Service pour récupérer l'instrument correspondant à l'id.
     *
     * @param id identifiant de l'instrument recherché
     *
     * @return l'instrument correspondant à l'id
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Instrument getInstrument(final int id) throws RepositoryException {

        return instrumentRepository.getInstrument(id);
    }

    /**
     * Service pour créer ou modifier un instrument.
     *
     * @param instrument instrument à créer ou modifier
     *
     * @return l'instrument créé ou modifié
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public Instrument saveInstrument(final Instrument instrument)
            throws RepositoryException {

        if (instrument.getIdInstrument() == null) {
            return instrumentRepository.createInstrument(instrument);
        } else {
            return instrumentRepository.updateInstrument(instrument);
        }
    }

    /**
     * Service pour supprimer un instrument.
     *
     * @param id identifiant de l'instrument à supprimer
     *
     * @throws RepositoryException si une action qui a échoué et qui nécessite
     *                             d'avertir l'utilisateur est survenue
     */
    public void deleteInstrument(final int id) throws RepositoryException {

        instrumentRepository.deleteInstrument(id);
    }
}
