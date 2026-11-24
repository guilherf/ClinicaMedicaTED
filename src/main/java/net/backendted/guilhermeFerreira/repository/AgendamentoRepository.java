package net.backendted.guilhermeFerreira.repository;


import net.backendted.guilhermeFerreira.model.Agendamento;
import net.backendted.guilhermeFerreira.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByData(LocalDate data);
    boolean existsByProfissionalIdAndDataAndHorarioAndStatusNot(Long profissionalId, LocalDate data, LocalTime horario, StatusAgendamento status);
}
