package net.backendted.guilhermeFerreira.controller;


import net.backendted.guilhermeFerreira.model.Convenio;
import net.backendted.guilhermeFerreira.repository.ConvenioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/convenios")
public class ConvenioController {

    private final ConvenioRepository repository;

    public ConvenioController(ConvenioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Convenio> listar() {
        return repository.findAll();
    }
}
