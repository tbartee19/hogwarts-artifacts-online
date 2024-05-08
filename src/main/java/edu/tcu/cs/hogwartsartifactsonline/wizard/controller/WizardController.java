package edu.tcu.cs.hogwartsartifactsonline.wizard;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wizards")
public class WizardController {
    private final WizardService wizardService;

    public WizardController(WizardService wizardService) {
        this.wizardService = wizardService;
    }

    @GetMapping
    public List<Wizard> findAll() {
        return wizardService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wizard> findById(@PathVariable Integer id) {
        Optional<Wizard> wizard = wizardService.findById(id);
        return wizard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Wizard> add(@RequestBody Wizard wizard) {
        Wizard newWizard = wizardService.save(wizard);
        return new ResponseEntity<>(newWizard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wizard> update(@PathVariable Integer id, @RequestBody Wizard wizard) {
        Optional<Wizard> updatedWizard = wizardService.update(id, wizard);
        return updatedWizard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = wizardService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
