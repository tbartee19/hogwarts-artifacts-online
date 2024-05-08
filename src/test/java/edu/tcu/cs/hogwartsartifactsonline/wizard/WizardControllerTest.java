package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardService;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

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

    @Test
    void testFindAll() throws Exception {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        when(wizardService.findAll()).thenReturn(Arrays.asList(w1, w2));

        mockMvc.perform(get("/api/wizards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$[1].name").value("Harry Potter"));
    }

    @Test
    void testFindById() throws Exception {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        when(wizardService.findById(1)).thenReturn(Optional.of(w1));

        mockMvc.perform(get("/api/wizards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Albus Dumbledore"));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(wizardService.findById(4)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/wizards/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAdd() throws Exception {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        when(wizardService.save(any(Wizard.class))).thenReturn(w1);

        mockMvc.perform(post("/api/wizards")
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

        when(wizardService.update(eq(1), any(WizardDto.class))).thenReturn(Optional.of(w1));

        mockMvc.perform(put("/api/wizards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wizardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Albus Dumbledore - Updated"));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        WizardDto wizardDto = new WizardDto(4, "Unknown Wizard", 0);

        when(wizardService.update(eq(4), any(WizardDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/wizards/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wizardDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        when(wizardService.delete(1)).thenReturn(true);

        mockMvc.perform(delete("/api/wizards/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(wizardService.delete(4)).thenReturn(false);

        mockMvc.perform(delete("/api/wizards/4"))
                .andExpect(status().isNotFound());
    }
}
