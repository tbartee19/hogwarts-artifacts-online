package edu.tcu.cs.hogwartsartifactsonline.wizard;


import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;

import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;



import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


class WizardServiceTest {

    @Mock
    private WizardRepository wizardRepository;

    @Mock
    private ArtifactRepository artifactRepository;
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

    @Test
    void testAssignArtifactSuccess() {

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");

       
        w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);

        w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        

        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a1));
        given(this.wizardRepository.findById(2)).willReturn(Optional.of(w2));


        // When
        this.wizardService.assignArtifact(2, "1250808601744904191");

        // Then
        assertThat(a1.getOwner().getId()).isEqualTo(2);
        assertThat(w2.getArtifacts()).contains(a1);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() {
        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());


        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(2, "1250808601744904191");
        });

        

        // Then
        assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find Artifact with Id 1250808601744904191 :(");

        
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() {

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");

       
        w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);

        

        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a1));
        given(this.wizardRepository.findById(2)).willReturn(Optional.empty());


        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(2, "1250808601744904191");
        });

        // Then
        assertThat(thrown)
            .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Wizard with Id 2 :(");


        assertThat(a1.getOwner().getId()).isEqualTo(1);
        assertThat(w1.getArtifacts()).contains(a1);
    }
}
