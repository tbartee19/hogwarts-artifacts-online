package edu.tcu.cs.hogwartsartifactsonline.wizard;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;

import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;

import java.util.List;


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
        try {
            Wizard wizard = wizardService.findById(id);
            return ResponseEntity.ok(wizard);
        } catch (ObjectNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Wizard> add(@RequestBody Wizard wizard) {
        Wizard newWizard = wizardService.save(wizard);
        return new ResponseEntity<>(newWizard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WizardDto> update(@PathVariable Integer id, @RequestBody WizardDto wizardDto) {
        try {
            Wizard updatedWizard = wizardService.update(id, wizardDto);
            WizardDto responseDto = new WizardDto(updatedWizard.getId(), updatedWizard.getName(), updatedWizard.getNumberOfArtifacts());
            return ResponseEntity.ok(responseDto);
        } catch (ObjectNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = wizardService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
