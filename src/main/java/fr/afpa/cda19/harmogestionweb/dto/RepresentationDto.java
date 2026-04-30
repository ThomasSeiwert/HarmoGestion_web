package fr.afpa.cda19.harmogestionweb.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 30/04/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepresentationDto {

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
}
