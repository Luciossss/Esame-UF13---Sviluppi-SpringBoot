package itt.marconi.fantacatalogo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity 
@Table(name = "calciatore")
@Data              // Genera getter e setter
@NoArgsConstructor // Genera il costruttore vuoto per jpa
@AllArgsConstructor // Genera il costruttore con tutti i campi (per i test)
public class Calciatore {

    @Id
    @Column(name = "codice")
    private String codice;

    @Column(name = "nome")
    private String nome;

    @Column(name = "squadra")
    private String squadra;

    @Column(name = "ruolo")
    private String ruolo;

    @Column(name = "quotazione")
    private int quotazione;

}