package net.backendted.guilhermeFerreira.controller;


import net.backendted.guilhermeFerreira.exception.ConflitoException;
import net.backendted.guilhermeFerreira.exception.RecursoNaoEncontradoException;
import net.backendted.guilhermeFerreira.model.Profissional;
import net.backendted.guilhermeFerreira.repository.ProfissionalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    private final ProfissionalRepository repository;

    public ProfissionalController(ProfissionalRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Profissional> listar(@RequestParam(defaultValue = "") String busca) {
        String termo = busca.trim().toLowerCase();
        return repository.findAll().stream()
                .filter(p -> termo.isEmpty()
                        || p.getNome().toLowerCase().contains(termo)
                        || p.getEspecialidade().toLowerCase().contains(termo)
                        || p.getCrm().toLowerCase().contains(termo))
                .sorted(Comparator.comparing(Profissional::getNome))
                .collect(Collectors.toList());
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registrar(@RequestBody Map<String, String> body) {
        String nome = body.getOrDefault("nome", "").trim();
        String especialidade = body.getOrDefault("especialidade", "").trim();
        String crm = body.getOrDefault("crm", "").trim().toUpperCase();
        String email = body.getOrDefault("email", "").trim().toLowerCase();
        String senha = body.getOrDefault("senha", "").trim();

        if (nome.isEmpty() || especialidade.isEmpty() || crm.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            throw new IllegalArgumentException("Preencha nome, especialidade, CRM, e-mail e senha.");
        }
        if (repository.findByEmailIgnoreCase(email).isPresent()) {
            throw new ConflitoException("Já existe um médico cadastrado com este e-mail.");
        }

        Profissional salvo = repository.save(new Profissional(nome, especialidade, crm, email, senha));
        return Map.of(
                "id", salvo.getId(),
                "nome", salvo.getNome(),
                "especialidade", salvo.getEspecialidade(),
                "crm", salvo.getCrm(),
                "email", salvo.getEmail(),
                "perfil", "MEDICO"
        );
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "").trim().toLowerCase();
        String senha = body.getOrDefault("senha", "").trim();

        Profissional prof = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("E-mail de médico não encontrado."));

        if (prof.getSenha() != null && !prof.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        return Map.of(
                "id", prof.getId(),
                "nome", prof.getNome(),
                "especialidade", prof.getEspecialidade(),
                "crm", prof.getCrm(),
                "email", prof.getEmail() != null ? prof.getEmail() : "",
                "perfil", "MEDICO"
        );
    }
}
