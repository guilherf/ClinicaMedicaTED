package net.backendted.guilhermeFerreira.repository;


import net.backendted.guilhermeFerreira.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<Profissional> findByEmailIgnoreCase(String email);
}
