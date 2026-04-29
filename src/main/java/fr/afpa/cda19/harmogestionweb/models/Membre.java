package fr.afpa.cda19.harmogestionweb.models;

import fr.afpa.cda19.harmogestionweb.dto.MembreDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Classe représentant un membre.
 *
 * @author Rodolphe BRUCKER
 * @version 1.0.0
 * @since 10/04/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membre {

    //----------------------------------------------------------------------------------------------
    // Attributs
    //----------------------------------------------------------------------------------------------

    /**
     * Identifiant du membre.
     */
    private Integer idMembre;

    /**
     * Nom du membre.
     */
    @NotBlank(message = "Un membre doit avoir un nom.")
    @Size(min = 3, max = 30,
            message = "Le nom du membre doit faire entre trois "
                    + "et trente caractères de long")
    private String nomMembre;

    /**
     * Prénom du membre.
     */
    @NotBlank(message = "Un membre doit avoir un prénom.")
    @Size(min = 3, max = 30,
            message = "Le prénom du membre doit faire entre trois "
                    + "et trente caractères de long.")
    private String prenomMembre;

    /**
     * Date d'inscription du membre.
     */
    @NotNull(message = "Un membre doit avoir une date d'inscription.")
    @PastOrPresent(message = "Une date d'inscription ne peut pas être future.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateInscriptionMembre;

    /**
     * Liste des instruments maitrisés.
     */
    @Size(max = 20, message = "Un membre peut maitriser au maximum"
            + " 20 instruments")
    private List<@Valid Instrument> instrumentsMaitrises;

    /**
     * Liste des instruments en apprentissage.
     */
    @Size(max = 10, message = "Un membre peut apprendre au maximum"
            + " 10 instruments")
    private List<@Valid Instrument> instrumentsAppris;

    //----------------------------------------------------------------------------------------------
    // Attributs de classe
    //----------------------------------------------------------------------------------------------

    /**
     * Comparator du membre par Nom.
     */
    public static final Comparator<Membre> COMPARATOR_NOM =
            Comparator.comparing(Membre::getNomMembre);

    //--------------------------------------------------------------------------
    // Méthodes
    //--------------------------------------------------------------------------

    /**
     * Méthode pour cloner un membre DTO en membre.
     *
     * @param membreDto membre à cloner.
     *
     * @return membre cloné.
     */
    public static Membre clone(final MembreDto membreDto) {

        Membre membreClone = new Membre();
        membreClone.setIdMembre(membreDto.getIdMembre());
        membreClone.setNomMembre(membreDto.getNomMembre());
        membreClone.setPrenomMembre(membreDto.getPrenomMembre());
        membreClone.setDateInscriptionMembre(membreDto.getDateInscriptionMembre());

        return membreClone;
    }
}
