package itt.marconi.fantacatalogo.controllers;

import itt.marconi.fantacatalogo.domain.Calciatore;
import itt.marconi.fantacatalogo.domain.CalciatoreForm;
import itt.marconi.fantacatalogo.services.CalciatoreService;
import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class FantaController {

    // Dependency Injection 
    @Autowired
    private CalciatoreService calciatoreService;

    // Endpoint per homepage
    @GetMapping
    public ModelAndView home(@RequestParam(name="search", required=false) String search) {
        ModelAndView mv = new ModelAndView("index");
        
        // Se il parametro 'search' è presente, filtro i risultati 
        if (search != null && !search.isEmpty()) {
            mv.addObject("calciatori", calciatoreService.filtra(search));
        } else {
            // Altrimenti passo alla webpage
            mv.addObject("calciatori", calciatoreService.getAll());
        }
        return mv;
    }

    // Endpoint per la richiesta GET
    @GetMapping("/new")
    public ModelAndView nuovoCalciatore() {
        // Passo un'istanza vuota dell'oggetto Calciatore alla pagina per il data-binding
        return new ModelAndView("form").addObject("calciatore", new CalciatoreForm());
    }

    @PostMapping("/new")
    public ModelAndView salvaCalciatore(
    @ModelAttribute @Valid CalciatoreForm contactForm, 
        BindingResult br, 
        RedirectAttributes ra,
        @RequestParam(value = "isEdit", required = false, defaultValue = "false") boolean isEdit // Aggiunto questo
    ) {
    
    Optional<Calciatore> esistente = calciatoreService.getByCode(contactForm.getCodice());

    // Se NON è una modifica, controlliamo i duplicati
    if (!isEdit && esistente.isPresent()) {
        br.rejectValue("codice", "duplicate", "Attenzione: Il codice " + contactForm.getCodice() + " è già assegnato a " + esistente.get().getNome());
    }

    if (br.hasErrors()) {
        return new ModelAndView("form").addObject("calciatore", contactForm);
    }

    calciatoreService.aggiungi(contactForm);
    ra.addFlashAttribute("conferma", "Operazione completata con successo!");
    
    return new ModelAndView("redirect:/item/" + contactForm.getCodice());
}
    // Endpoint per mostrare un singolo elemento tramite il codice 
    @GetMapping("/item/{code}")
    public ModelAndView dettaglio(@PathVariable("code") String code) {
        
        Optional<Calciatore> oc = calciatoreService.getByCode(code);

        // Controllo se il dato è presente nel database
        if (oc.isPresent()) {
            return new ModelAndView("detail").addObject("p", oc.get());
        } else {
            // Se il codice non esiste
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Calciatore non trovato");
        }
    }

    // Endpoint per l'eliminazione di un calciatore 
    @GetMapping("/delete/{code}")
    public ModelAndView elimina(@PathVariable("code") String code, RedirectAttributes ra) {
        calciatoreService.eliminaPerCodice(code);
        
        // Feedback per l'utente dopo la rimozione
        ra.addFlashAttribute("eliminato", "Calciatore rimosso dal catalogo.");
        
        return new ModelAndView("redirect:/");
    }


    @GetMapping("/edit/{code}")
    public ModelAndView modifica(@PathVariable("code") String code) {
        Optional<Calciatore> oc = calciatoreService.getByCode(code);

        if (oc.isPresent()) {
            Calciatore c = oc.get();
            
           
            CalciatoreForm cf = new CalciatoreForm();
            cf.setCodice(c.getCodice());
            cf.setNome(c.getNome());
            cf.setSquadra(c.getSquadra());
            cf.setRuolo(c.getRuolo());
            cf.setQuotazione(c.getQuotazione());

            return new ModelAndView("form").addObject("calciatore", cf);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Calciatore non trovato");
        }
    }

    
    @GetMapping("/clear")
    public ModelAndView svuota() {
        calciatoreService.svuotaCatalogo();
        return new ModelAndView("redirect:/");
    }
}