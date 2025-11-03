package com.example.pagination.service;


import com.example.pagination.dto.PersonDto;
import com.example.pagination.model.Person;
import com.example.pagination.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    /**
     *
     * @param personDto entry person information
     * @return person information
     */
    public PersonDto createPerson(PersonDto personDto) {
        personRepository.save(
                new Person(null, personDto.pseudo(), personDto.profession())
        );
        return new PersonDto(personDto.pseudo(), personDto.profession());
    }

    /**
     * retreive all person with pagination
     * @param pageNumber of element
     * @return content of pagination
     */
    public List<PersonDto> retreivePerson(Integer pageNumber) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Person> personPage = personRepository.findAll(pageable);
        return personPage.map(person -> new PersonDto(person.getPseudo(), person.getProfession()))
                .getContent();
    }
}
