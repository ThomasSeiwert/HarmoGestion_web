package fr.afpa.cda19.harmogestionweb.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe représentant un instrument.
 *
 * @author Cédric DIDIER
 * @version 1.0.0
 * @since 10/04/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instrument {

    //--------------------------------------------------------------------------
    // Attributs
    //--------------------------------------------------------------------------

    /**
     * Identifiant de l'instrument.
     */
    private Integer idInstrument;

    /**
     * Nom de l'instrument.
     */
    @NotBlank(message = "Un instrument doit avoir un nom")
    @Size(min = 3, max = 50,
          message = "Le nom de l'instrument doit faire entre trois "
                    + "et cinquante caractères de long")
    private String libelleInstrument;
}
