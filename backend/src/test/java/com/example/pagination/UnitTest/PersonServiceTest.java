package com.example.pagination.UnitTest;

import com.example.pagination.dto.PersonDto;
import com.example.pagination.model.Person;
import com.example.pagination.repository.PersonRepository;
import com.example.pagination.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    @DisplayName("createPerson devrait sauvegarder et retourner un DTO")
    void createPerson_shouldSaveAndReturnDto() {
        // Given
        PersonDto inputDto = new PersonDto("testUser", "Developer");
        Person savedPerson = new Person(1L, "testUser", "Developer");

        // Mocking the repository save method
        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // When
        PersonDto resultDto = personService.createPerson(inputDto);

        // Then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.pseudo()).isEqualTo(savedPerson.getPseudo());
        assertThat(resultDto.profession()).isEqualTo(savedPerson.getProfession());

        // Verify that the save method was called on the repository
        verify(personRepository).save(any(Person.class));
    }

    @Test
    @DisplayName("retreivePerson devrait retourner une liste paginée de DTOs")
    void retreivePerson_shouldReturnPaginatedListOfDtos() {
        // Given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Person> personList = List.of(
                new Person(1L, "user1", "Engineer"),
                new Person(2L, "user2", "Doctor")
        );
        Page<Person> personPage = new PageImpl<>(personList, pageable, personList.size());

        // Mocking the repository findAll method
        when(personRepository.findAll(pageable)).thenReturn(personPage);

        // When
        List<PersonDto> resultList = personService.retreivePerson(pageNumber);

        // Then
        assertThat(resultList).isNotNull();
        assertThat(resultList).hasSize(2);
        assertThat(resultList.get(0).pseudo()).isEqualTo("user1");
        assertThat(resultList.get(1).profession()).isEqualTo("Doctor");

        verify(personRepository).findAll(pageable);
    }
}