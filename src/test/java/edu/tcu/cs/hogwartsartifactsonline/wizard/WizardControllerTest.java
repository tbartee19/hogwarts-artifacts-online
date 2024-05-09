package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardService;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WizardController.class)
class WizardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WizardService wizardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Test
    void testFindAll() throws Exception {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        when(wizardService.findAll()).thenReturn(Arrays.asList(w1, w2));

        mockMvc.perform(get(this.baseUrl + "/wizards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$[1].name").value("Harry Potter"));
    }

    @Test
    void testFindById() throws Exception {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        when(wizardService.findById(1)).thenReturn(w1);

        mockMvc.perform(get(this.baseUrl + "/wizards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Albus Dumbledore"));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        // Simulate the ObjectNotFoundException being thrown
        when(wizardService.findById(4)).thenThrow(new ObjectNotFoundException("Wizard", 4));

        mockMvc.perform(get(this.baseUrl + "/wizards/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAdd() throws Exception {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        when(wizardService.save(any(Wizard.class))).thenReturn(w1);

        mockMvc.perform(post(this.baseUrl + "/wizards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(w1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Albus Dumbledore"));
    }

    @Test
    void testUpdate() throws Exception {
        WizardDto wizardDto = new WizardDto(1, "Albus Dumbledore - Updated", 2);

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore - Updated");

        when(wizardService.update(eq(1), any(WizardDto.class))).thenReturn(w1);

        mockMvc.perform(put(this.baseUrl + "/wizards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wizardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Albus Dumbledore - Updated"));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        WizardDto wizardDto = new WizardDto(4, "Unknown Wizard", 0);

        // Simulate the ObjectNotFoundException being thrown
        when(wizardService.update(eq(4), any(WizardDto.class)))
                .thenThrow(new ObjectNotFoundException("Wizard", 4));

        mockMvc.perform(put(this.baseUrl + "/wizards/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wizardDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        when(wizardService.delete(1)).thenReturn(true);

        mockMvc.perform(delete(this.baseUrl + "/wizards/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        // Simulate the ObjectNotFoundException being thrown
        when(wizardService.delete(4)).thenThrow(new ObjectNotFoundException("Wizard", 4));

        mockMvc.perform(delete(this.baseUrl + "/wizards/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        doNothing().when(this.wizardService).assignArtifact(2, "1250808601744904191");

        this.mockMvc.perform(put(this.baseUrl + "/wizards/2/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
