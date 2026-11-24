package net.backendted.guilhermeFerreira.dto;

import net.backendted.guilhermeFerreira.model.TipoConsulta;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequest(
        String nomePaciente,
        String telefone,
        Long pacienteId,
        Long convenioId,
        Long profissionalId,
        LocalDate data,
        LocalTime horario,
        TipoConsulta tipo
) {}
