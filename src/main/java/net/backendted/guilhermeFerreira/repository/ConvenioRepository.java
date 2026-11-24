package net.backendted.guilhermeFerreira.repository;


import net.backendted.guilhermeFerreira.model.Convenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenioRepository extends JpaRepository<Convenio, Long> {
}
