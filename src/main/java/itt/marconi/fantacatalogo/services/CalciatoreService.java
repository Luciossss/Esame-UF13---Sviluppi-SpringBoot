package itt.marconi.fantacatalogo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itt.marconi.fantacatalogo.domain.Calciatore;
import itt.marconi.fantacatalogo.domain.CalciatoreForm;
import itt.marconi.fantacatalogo.repositories.CalciatoreRepository;

// Un service si interfaccia tra Controller e Repository
// Sceglie come e quali dati leggere, salvare, eliminare...
@Service
public class CalciatoreService {
    
    // Dependency Injection 
    @Autowired
    private CalciatoreRepository calciatoreRepo;

    // Metodo per salvare un nuovo calciatore partendo dal form
    public Calciatore aggiungi(CalciatoreForm calciatoreForm) {

        Calciatore c = mapCalciatore(calciatoreForm);
        return calciatoreRepo.save(c);
    }

    // Metodo privato per mappare i dati dal Form all'Entità 
    private Calciatore mapCalciatore(CalciatoreForm form) {

        Calciatore c = new Calciatore();
        c.setCodice(form.getCodice());
        c.setNome(form.getNome());
        c.setSquadra(form.getSquadra());
        c.setRuolo(form.getRuolo());
        c.setQuotazione(form.getQuotazione());
        
        return c;
    }

    // Ritorna la lista completa per la homepage
    public List<Calciatore> getAll() {
        return calciatoreRepo.findAll();
    }

    // Filtro di ricerca per nome 
    public List<Calciatore> filtra(String search) {
        return calciatoreRepo.findByNomeContainingIgnoreCase(search);
    }

    // Recupera un singolo calciatore tramite codice
    public Optional<Calciatore> getByCode(String code) {
        return calciatoreRepo.findById(code);
    }

    // Elimina un calciatore
    public void eliminaPerCodice(String code) {
        calciatoreRepo.deleteById(code);
    }

    // Svuota tutto il catalogo
    public void svuotaCatalogo() {
        calciatoreRepo.deleteAll();
    }
}