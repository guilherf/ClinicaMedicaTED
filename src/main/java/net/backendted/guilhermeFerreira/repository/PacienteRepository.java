package net.backendted.guilhermeFerreira.repository;


import net.backendted.guilhermeFerreira.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByEmailIgnoreCase(String email);
    Optional<Paciente> findFirstByNomeIgnoreCase(String nome);
}
