package itt.marconi.fantacatalogo.controllers;

import itt.marconi.fantacatalogo.domain.CalciatoreForm;
import itt.marconi.fantacatalogo.services.CalciatoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class FantaController {

    @Autowired
    private CalciatoreService calciatoreService;

    @GetMapping
    public ModelAndView home(@RequestParam(name="search", required=false) String search) {
        ModelAndView mv = new ModelAndView("index");
        
        if (search != null && !search.isEmpty()) {
            mv.addObject("calciatori", calciatoreService.filtra(search));
        } else {
            mv.addObject("calciatori", calciatoreService.getAll());
        }
        return mv;
    }

  
    @GetMapping("/new")
    public ModelAndView nuovoCalciatore() {
        return new ModelAndView("form").addObject("calciatore", new CalciatoreForm());
    }

    @PostMapping("/new")
    public ModelAndView salvaCalciatore(
        @ModelAttribute @Valid CalciatoreForm contactForm, 
        BindingResult br, 
        RedirectAttributes ra,
        @RequestParam(value = "isEdit", required = false, defaultValue = "false") boolean isEdit
    ) {
    
        
        if (!isEdit && calciatoreService.esisteCodice(contactForm.getCodice())) {
            br.rejectValue("codice", "duplicate", "Attenzione: Il codice " + contactForm.getCodice() + " è già assegnato.");
        }

        if (br.hasErrors()) {
            return new ModelAndView("form").addObject("calciatore", contactForm);
        }

        calciatoreService.aggiungi(contactForm);
        ra.addFlashAttribute("conferma", "Operazione completata con successo!");
        
        return new ModelAndView("redirect:/item/" + contactForm.getCodice());
    }

  
    @GetMapping("/item/{code}")
    public ModelAndView dettaglio(@PathVariable("code") String code) {
       
        CalciatoreForm calciatore = calciatoreService.getByCode(code);
        return new ModelAndView("detail").addObject("p", calciatore);
    }

  
    @GetMapping("/delete/{code}")
    public ModelAndView elimina(@PathVariable("code") String code, RedirectAttributes ra) {
        calciatoreService.eliminaPerCodice(code);
        ra.addFlashAttribute("eliminato", "Calciatore rimosso dal catalogo.");
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit/{code}")
    public ModelAndView modifica(@PathVariable("code") String code) {
        // Il service lancia già un 404 se non lo trova
        CalciatoreForm cf = calciatoreService.getByCode(code);
        return new ModelAndView("form").addObject("calciatore", cf);
    }

    @GetMapping("/clear")
    public ModelAndView svuota() {
        calciatoreService.svuotaCatalogo();
        return new ModelAndView("redirect:/");
    }
}