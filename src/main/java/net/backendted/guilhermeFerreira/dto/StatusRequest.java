package net.backendted.guilhermeFerreira.dto;


import net.backendted.guilhermeFerreira.model.StatusAgendamento;

public record StatusRequest(
        StatusAgendamento status,
        String perfil,
        String queixa,
        String diagnostico,
        String prescricao
) {}
