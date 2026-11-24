package net.backendted.guilhermeFerreira.dto;


import net.backendted.guilhermeFerreira.model.Agendamento;

import java.time.format.DateTimeFormatter;

public record AgendamentoResponse(
        Long id,
        Long pacienteId,
        String pacienteNome,
        String pacienteIniciais,
        String telefone,
        Long profissionalId,
        String profissionalNome,
        String especialidade,
        String crm,
        Long convenioId,
        String convenioNome,
        String data,
        String horario,
        String tipo,
        String tipoDescricao,
        String status,
        String statusDescricao,
        boolean confirmadoPaciente,
        boolean confirmadoMedico,
        String queixa,
        String diagnostico,
        String prescricao,
        String atendidoEm
) {
    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public static AgendamentoResponse from(Agendamento a) {
        return new AgendamentoResponse(
                a.getId(),
                a.getPaciente().getId(),
                a.getPaciente().getNome(),
                a.getPaciente().getIniciais(),
                a.getPaciente().getTelefone(),
                a.getProfissional().getId(),
                a.getProfissional().getNome(),
                a.getProfissional().getEspecialidade(),
                a.getProfissional().getCrm(),
                a.getConvenio().getId(),
                a.getConvenio().getNome(),
                a.getData().toString(),
                a.getHorario().format(HORA_FMT),
                a.getTipo().name(),
                a.getTipo().getDescricao(),
                a.getStatus().name(),
                a.getStatus().getDescricao(),
                a.isConfirmadoPaciente(),
                a.isConfirmadoMedico(),
                a.getQueixa() != null ? a.getQueixa() : "",
                a.getDiagnostico() != null ? a.getDiagnostico() : "",
                a.getPrescricao() != null ? a.getPrescricao() : "",
                a.getAtendidoEm() != null ? a.getAtendidoEm() : ""
        );
    }
}
