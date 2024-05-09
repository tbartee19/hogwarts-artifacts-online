package edu.tcu.cs.hogwartsartifactsonline.wizard;

import org.springframework.stereotype.Service;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;

import java.util.List;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    public Wizard findById(Integer id) {
        return wizardRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Wizard", id));
    }

    public Wizard save(Wizard wizard) {
        return wizardRepository.save(wizard);
    }

    public Wizard update(Integer id, WizardDto wizardDto) {
        return wizardRepository.findById(id)
                .map(existingWizard -> {
                    existingWizard.setName(wizardDto.name());
                    return wizardRepository.save(existingWizard);
                })
                .orElseThrow(() -> new ObjectNotFoundException("Wizard", id));
    }

    public boolean delete(Integer id) {
        Wizard wizard = wizardRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Wizard", id));
        wizard.removeAllArtifacts();
        wizardRepository.deleteById(id);
        return true;
    }

    public void assignArtifact(Integer id, String artifactId) {
        Artifact artifactToBeAssigned = this.artifactRepository.findById(artifactId)
            .orElseThrow(() -> new ObjectNotFoundException("Artifact",artifactId));

        Wizard wizard = this.wizardRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Wizard",id));

        if(artifactToBeAssigned.getOwner() != null) {
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);

        }
        wizard.addArtifact(artifactToBeAssigned);

        artifactToBeAssigned.setOwner(wizard);

        artifactRepository.save(artifactToBeAssigned);
        wizardRepository.save(wizard);
        
    }
}
