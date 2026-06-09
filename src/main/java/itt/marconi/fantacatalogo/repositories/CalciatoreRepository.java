package itt.marconi.fantacatalogo.repositories;

import itt.marconi.fantacatalogo.domain.Calciatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalciatoreRepository extends JpaRepository<Calciatore, String> {
    // Questo servirà per il punto 5b (Filtro ricerca)
    List<Calciatore> findByNomeContainingIgnoreCase(String nome);
}