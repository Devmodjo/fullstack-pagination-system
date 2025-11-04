package com.example.pagination.IntegrationTest;

import com.example.pagination.dto.PersonDto;
import com.example.pagination.model.Person;
import com.example.pagination.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        // Clean up the database after each test to ensure isolation
        personRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/person devrait créer une personne et retourner 200 OK")
    void createPerson_shouldCreatePersonAndReturnOk() throws Exception {
        // Given
        PersonDto personDto = new PersonDto("integTestUser", "QA Engineer");

        // When & Then
        mockMvc.perform(post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pseudo", is("integTestUser")))
                .andExpect(jsonPath("$.profession", is("QA Engineer")));

        // Verify data in database
        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(1);
        assertThat(people.get(0).getPseudo()).isEqualTo("integTestUser");
    }

    @Test
    @DisplayName("GET /api/person devrait retourner une liste paginée de personnes")
    void retreivePerson_shouldReturnPaginatedList() throws Exception {
        // Given: Pre-populate the database
        personRepository.save(new Person(null, "user1", "Dev"));
        personRepository.save(new Person(null, "user2", "Ops"));

        // When & Then
        mockMvc.perform(get("/api/person")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pseudo", is("user1")))
                .andExpect(jsonPath("$[1].pseudo", is("user2")));
    }
}