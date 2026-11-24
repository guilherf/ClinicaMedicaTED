package net.backendted.guilhermeFerreira.controller;


import net.backendted.guilhermeFerreira.exception.ConflitoException;
import net.backendted.guilhermeFerreira.exception.RecursoNaoEncontradoException;
import net.backendted.guilhermeFerreira.model.Paciente;
import net.backendted.guilhermeFerreira.repository.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteRepository repository;

    public PacienteController(PacienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Paciente> listar() {
        return repository.findAll();
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registrar(@RequestBody Map<String, String> body) {
        String nome = body.getOrDefault("nome", "").trim();
        String telefone = body.getOrDefault("telefone", "").trim();
        String email = body.getOrDefault("email", "").trim().toLowerCase();
        String senha = body.getOrDefault("senha", "").trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            throw new IllegalArgumentException("Preencha nome, e-mail e senha.");
        }
        if (repository.findByEmailIgnoreCase(email).isPresent()) {
            throw new ConflitoException("Já existe um paciente cadastrado com este e-mail.");
        }

        Paciente salvo = repository.save(new Paciente(nome, telefone, email, senha));
        return Map.of(
                "id", salvo.getId(),
                "nome", salvo.getNome(),
                "iniciais", salvo.getIniciais(),
                "telefone", salvo.getTelefone(),
                "email", salvo.getEmail(),
                "perfil", "PACIENTE"
        );
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "").trim().toLowerCase();
        String senha = body.getOrDefault("senha", "").trim();

        Paciente pac = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("E-mail de paciente não encontrado."));

        if (pac.getSenha() != null && !pac.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        return Map.of(
                "id", pac.getId(),
                "nome", pac.getNome(),
                "iniciais", pac.getIniciais(),
                "telefone", pac.getTelefone() != null ? pac.getTelefone() : "",
                "email", pac.getEmail() != null ? pac.getEmail() : "",
                "perfil", "PACIENTE"
        );
    }
}
