package edu.tcu.cs.hogwartsartifactsonline.wizard;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    public Optional<Wizard> findById(Integer id) {
        return wizardRepository.findById(id);
    }

    public Wizard save(Wizard wizard) {
        return wizardRepository.save(wizard);
    }

    public Optional<Wizard> update(Integer id, Wizard updatedWizard) {
        return wizardRepository.findById(id).map(existingWizard -> {
            existingWizard.setName(updatedWizard.getName());
            existingWizard.setArtifacts(updatedWizard.getArtifacts());
            return wizardRepository.save(existingWizard);
        });
    }

    public boolean delete(Integer id) {
        if (wizardRepository.existsById(id)) {
            wizardRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
