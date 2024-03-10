package com.techchallenge.restaurant.api.findfood.controller;

import com.techchallenge.restaurant.api.findfood.dto.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.entities.Restaurante;
import com.techchallenge.restaurant.api.findfood.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteService service;

    @PostMapping
    public ResponseEntity<RestauranteDTO> save(@RequestBody RestauranteDTO restauranteDTO) {
        restauranteDTO = service.save(restauranteDTO);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(restauranteDTO);
    }

    @GetMapping("/pesquisa")
    public ResponseEntity<List<Restaurante>> findRestaurantes(@RequestParam(name = "nome", required = false, defaultValue = "") String nome,
                                                              @RequestParam(name = "localizacao", required = false, defaultValue = "") String localizacao,
                                                              @RequestParam(name = "tipoCozinha", required = false, defaultValue = "") String tipoCozinha) {
        var restaurante = service.findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(nome, localizacao, tipoCozinha);
        return ResponseEntity.ok(restaurante);
    }

    @GetMapping
    public ResponseEntity<Collection<RestauranteDTO>> findAll() {
        var restaurantes = service.findAll();
        return ResponseEntity.ok(restaurantes);
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<RestauranteDTO> update(@PathVariable Long restauranteId, @RequestBody RestauranteDTO restauranteDTO) {
        restauranteDTO = service.update(restauranteId, restauranteDTO);
        return ResponseEntity.ok(restauranteDTO);
    }

    @DeleteMapping("/{restauranteId}")
    public ResponseEntity<Void> delete(@PathVariable Long restauranteId) {
        service.delete(restauranteId);
        return ResponseEntity.noContent().build();
    }
}
