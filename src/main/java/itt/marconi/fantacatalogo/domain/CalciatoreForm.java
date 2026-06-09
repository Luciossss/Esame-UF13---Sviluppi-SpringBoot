package itt.marconi.fantacatalogo.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //genere automaticamente getter e setter
@NoArgsConstructor // Genera il costruttore vuoto per jpa
public class CalciatoreForm {

    @NotEmpty(message = "Il codice è obbligatorio")
    @Size(min = 2, max = 10, message = "Il codice deve essere lungo tra 2 e 10 caratteri")
    private String codice;

    @NotEmpty(message = "Il nome non può essere vuoto")
    private String nome;

    @NotEmpty(message = "Specificare la squadra")
    private String squadra;

    @NotEmpty(message = "Seleziona un ruolo")
    private String ruolo;

    @Min(value = 1, message = "La quotazione minima è 1 FantaMilione")
    @Max(value = 500, message = "La quotazione massima è 500 FantaMilioni")
    private int quotazione;

}