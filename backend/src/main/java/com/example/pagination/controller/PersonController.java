package com.example.pagination.controller;


import com.example.pagination.dto.PersonDto;
import com.example.pagination.model.Person;
import com.example.pagination.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
@Tag(name = "Person API", description = "API pour la gestion des personnes")
public class PersonController {

    private final PersonService personService;

    @Operation(summary = "Créer une nouvelle personne", description = "Enregistre une nouvelle personne dans la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personne créée avec succès",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Données d'entrée invalides",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto) {
        return ResponseEntity.ok(personService.createPerson(personDto));
    }

    @Operation(summary = "Récupérer une liste paginée de personnes", description = "Fournit une liste de personnes avec un système de pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des personnes récupérée avec succès",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonDto.class)) })
    })
    @GetMapping
    public ResponseEntity<List<PersonDto>> retreivePerson(
            @Parameter(description = "Numéro de la page à récupérer (commence à 0)") @RequestParam(name = "page", defaultValue = "0") int pageNumber) {
        return ResponseEntity.ok(personService.retreivePerson(pageNumber));
    }
}
