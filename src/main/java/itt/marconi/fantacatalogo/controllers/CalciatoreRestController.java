package itt.marconi.fantacatalogo.controllers;

import itt.marconi.fantacatalogo.dtos.APIResponse;
import itt.marconi.fantacatalogo.domain.CalciatoreForm;
import itt.marconi.fantacatalogo.services.CalciatoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/calciatori")
public class CalciatoreRestController {

    @Autowired
    private CalciatoreService calciatoreService;

    @GetMapping
    public ResponseEntity<APIResponse<List<CalciatoreForm>>> getAllCalciatori(@RequestParam(name="search", required=false) String search) {
        List<CalciatoreForm> risultati;
        
        if (search != null && !search.isEmpty()) {
            risultati = calciatoreService.filtra(search);
        } else {
            risultati = calciatoreService.getAll();
        }

        APIResponse<List<CalciatoreForm>> response = APIResponse.<List<CalciatoreForm>>builder()
                .status("success")
                .data(risultati)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<APIResponse<CalciatoreForm>> getCalciatoreByCode(@PathVariable String code) {
        CalciatoreForm calciatore = calciatoreService.getByCode(code);

        APIResponse<CalciatoreForm> response = APIResponse.<CalciatoreForm>builder()
                .status("success")
                .data(calciatore)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<APIResponse<CalciatoreForm>> createCalciatore(@Valid @RequestBody CalciatoreForm nuovoCalciatore) {
        if (calciatoreService.esisteCodice(nuovoCalciatore.getCodice())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il codice " + nuovoCalciatore.getCodice() + " è già assegnato.");
        }

        CalciatoreForm saved = calciatoreService.aggiungi(nuovoCalciatore);

        APIResponse<CalciatoreForm> response = APIResponse.<CalciatoreForm>builder()
                .status("success")
                .data(saved)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<APIResponse<String>> deleteCalciatore(@PathVariable String code) {
        calciatoreService.eliminaPerCodice(code);

        APIResponse<String> response = APIResponse.<String>builder()
                .status("success")
                .data("Calciatore con codice " + code + " rimosso con successo dal catalogo.")
                .build();

        return ResponseEntity.ok(response);
    }
}