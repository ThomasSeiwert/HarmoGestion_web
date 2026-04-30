package fr.afpa.cda19.harmogestionweb.models;

import fr.afpa.cda19.harmogestionweb.dto.RepresentationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Model des représentations.
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 30/04/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Representation {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

    /**
     * Identifiant.
     */
    private Integer idRepresentation;

    /**
     * Nom de la représentation.
     */
    @NotNull(message = "La représentation doit avoir un nom")
    @Size(min = 3, max = 50,
            message = "Le nom de la représentation doit faire entre trois "
                    + "et cinquante caractères de long")
    private String nomRepresentation;

    /**
     * Date et heure de la représentation.
     */
    @NotNull(message = "La représentation doit avoir une date")
    @FutureOrPresent(message = "La représentation doit être à une date future")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateRepresentation;

    /**
     * Liste des membres participants.
     */
    @NotNull(message = "La représentation doit avoir des participants")
    @Size(min = 1, message = "Il doit y avoir au moins un participant")
    private List<@Valid Membre> participants;

    /**
     * Liste des instruments joués.
     */
    @NotNull(message = "La représentation doit avoir des instruments")
    @Size(min = 1, message = "Il doit y avoir au moins un instrument")
    private List<@Valid Instrument> instruments;

    //----------------------------------------------------------------------------------------------
    // Attributs de classe
    //----------------------------------------------------------------------------------------------

    /**
     * Comparator de la représentation par date.
     */
    public static final Comparator<Representation> COMPARATOR_DATE =
            Comparator.comparing(Representation::getDateRepresentation);

    //----------------------------------------------------------------------------------------------
    // Méthodes
    //----------------------------------------------------------------------------------------------

    /**
     * Méthode pour cloner une représentation DTO en représentation persistante.
     *
     * @param representationDTO représentation à cloner.
     *
     * @return représentation clonée.
     */
    public static Representation clone(final RepresentationDto representationDTO) {

        Representation representationClone = new Representation();
        representationClone.setIdRepresentation(representationDTO.getIdRepresentation());
        representationClone.setNomRepresentation(representationDTO.getNomRepresentation());
        representationClone.setDateRepresentation(representationDTO.getDateRepresentation());

        return representationClone;
    }
}
