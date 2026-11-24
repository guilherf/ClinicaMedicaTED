package net.backendted.guilhermeFerreira.controller;


import net.backendted.guilhermeFerreira.dto.AgendamentoRequest;
import net.backendted.guilhermeFerreira.dto.AgendamentoResponse;
import net.backendted.guilhermeFerreira.dto.StatusRequest;
import net.backendted.guilhermeFerreira.service.AgendamentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<AgendamentoResponse> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "") String especialidade,
            @RequestParam(defaultValue = "horario") String ordenarPor,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long profissionalId
    ) {
        return service.listar(data, busca, especialidade, ordenarPor, pacienteId, profissionalId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponse criar(@RequestBody AgendamentoRequest request) {
        return service.criar(request);
    }

    @PatchMapping("/{id}/status")
    public AgendamentoResponse atualizarStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        return service.atualizarStatus(id, request);
    }

    @PostMapping("/{id}/confirmar")
    public AgendamentoResponse confirmar(@PathVariable Long id, @RequestParam(defaultValue = "PACIENTE") String perfil) {
        return service.confirmar(id, perfil);
    }

    @PostMapping("/{id}/iniciar-atendimento")
    public AgendamentoResponse iniciarAtendimento(@PathVariable Long id) {
        return service.iniciarAtendimento(id);
    }

    @PostMapping("/{id}/finalizar-atendimento")
    public AgendamentoResponse finalizarAtendimento(@PathVariable Long id, @RequestBody StatusRequest request) {
        return service.finalizarAtendimento(id, request);
    }

    @PostMapping("/{id}/cancelar")
    public AgendamentoResponse cancelar(@PathVariable Long id) {
        return service.cancelar(id);
    }
}
