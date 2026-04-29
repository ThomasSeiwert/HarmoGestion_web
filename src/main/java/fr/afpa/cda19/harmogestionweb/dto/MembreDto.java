package fr.afpa.cda19.harmogestionweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 28/04/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembreDto {

    /**
     * Identifiant du membre.
     */
    private Integer idMembre;

    /**
     * Nom du membre.
     */
    private String nomMembre;

    /**
     * Prénom du membre.
     */
    private String prenomMembre;

    /**
     * Date d'inscription du membre.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateInscriptionMembre;
}
