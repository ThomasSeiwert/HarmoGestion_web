package fr.afpa.cda19.harmogestionweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 *
 * @author Seiwert Thomas
 * @version 0.0.1
 * @since 28/04/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursDto {

    /**
     * Identifiant.
     */
    private Integer idCours;

    /**
     * Date du cours.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateCours;

    /**
     * Durée du cours (en min).
     */
    private byte dureeCours;
}
