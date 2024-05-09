package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardService;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardRepository;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class WizardServiceTest {

    @Mock
    private WizardRepository wizardRepository;

    @InjectMocks
    private WizardService wizardService;

    private Wizard w1, w2, w3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Artifacts setup
        Artifact a1 = new Artifact();
        a1.setId("1");
        a1.setName("Deluminator");

        Artifact a2 = new Artifact();
        a2.setId("2");
        a2.setName("Invisibility Cloak");

        // Wizards setup
        w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);

        w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);

        w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
    }

    @Test
    void testFindAll() {
        List<Wizard> expectedWizards = Arrays.asList(w1, w2, w3);
        when(wizardRepository.findAll()).thenReturn(expectedWizards);

        List<Wizard> result = wizardService.findAll();
        assertEquals(3, result.size());
        assertTrue(result.containsAll(expectedWizards));
    }

    @Test
    void testFindById() {
        when(wizardRepository.findById(1)).thenReturn(Optional.of(w1));

        Wizard result = wizardService.findById(1);
        assertEquals("Albus Dumbledore", result.getName());
    }

    @Test
    void testFindById_NotFound() {
        when(wizardRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> wizardService.findById(4));
    }

    @Test
    void testSave() {
        when(wizardRepository.save(any(Wizard.class))).thenReturn(w1);

        Wizard result = wizardService.save(w1);
        assertEquals("Albus Dumbledore", result.getName());
    }

    @Test
    void testUpdate() {
        when(wizardRepository.findById(1)).thenReturn(Optional.of(w1));
        when(wizardRepository.save(any(Wizard.class))).thenReturn(w1);

        WizardDto updatedWizardDto = new WizardDto(1, "Albus Dumbledore - Updated", 2);

        Wizard result = wizardService.update(1, updatedWizardDto);
        assertEquals("Albus Dumbledore - Updated", result.getName());
    }

    @Test
    void testUpdate_NotFound() {
        when(wizardRepository.findById(4)).thenReturn(Optional.empty());

        WizardDto nonExistentWizardDto = new WizardDto(4, "Unknown Wizard", 0);

        assertThrows(ObjectNotFoundException.class, () -> wizardService.update(4, nonExistentWizardDto));
    }

    @Test
    void testDelete() {
        when(wizardRepository.findById(1)).thenReturn(Optional.of(w1));
        doNothing().when(wizardRepository).deleteById(1);

        boolean result = wizardService.delete(1);
        assertTrue(result);
        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDelete_NotFound() {
        when(wizardRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> wizardService.delete(4));
        verify(wizardRepository, never()).deleteById(4);
    }
}
