package itt.marconi.fantacatalogo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import itt.marconi.fantacatalogo.domain.Calciatore;
import itt.marconi.fantacatalogo.domain.CalciatoreForm;
import itt.marconi.fantacatalogo.repositories.CalciatoreRepository;

@Service
public class CalciatoreService {
    
    @Autowired
    private CalciatoreRepository calciatoreRepo;

    // Aggiunge o aggiorna un calciatore restituendo il dto
    public CalciatoreForm aggiungi(CalciatoreForm form) {
        Calciatore c = mapToEntity(form);
        Calciatore salvato = calciatoreRepo.save(c);
        return mapToDto(salvato);
    }

    // Ritorna la lista completa  in dto
    public List<CalciatoreForm> getAll() {
        return calciatoreRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Filtro di ricerca in dto
    public List<CalciatoreForm> filtra(String search) {
        return calciatoreRepo.findByNomeContainingIgnoreCase(search).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Recupera un calciatore o lancia un 404
    public CalciatoreForm getByCode(String code) {
        Calciatore c = calciatoreRepo.findById(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Calciatore non trovato con codice: " + code));
        return mapToDto(c);
    }

    // double check
    public boolean esisteCodice(String code) {
        return calciatoreRepo.existsById(code);
    }

    public void eliminaPerCodice(String code) {
        if (!calciatoreRepo.existsById(code)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile eliminare: calciatore inesistente");
        }
        calciatoreRepo.deleteById(code);
    }

    public void svuotaCatalogo() {
        calciatoreRepo.deleteAll();
    }

    //mappatura dto
    private Calciatore mapToEntity(CalciatoreForm form) {
        Calciatore c = new Calciatore();
        c.setCodice(form.getCodice());
        c.setNome(form.getNome());
        c.setSquadra(form.getSquadra());
        c.setRuolo(form.getRuolo());
        c.setQuotazione(form.getQuotazione());
        return c;
    }

    private CalciatoreForm mapToDto(Calciatore entity) {
        CalciatoreForm form = new CalciatoreForm();
        form.setCodice(entity.getCodice());
        form.setNome(entity.getNome());
        form.setSquadra(entity.getSquadra());
        form.setRuolo(entity.getRuolo());
        form.setQuotazione(entity.getQuotazione());
        return form;
    }
}