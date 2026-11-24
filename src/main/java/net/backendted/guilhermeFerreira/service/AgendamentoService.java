package net.backendted.guilhermeFerreira.service;

import net.backendted.guilhermeFerreira.dto.AgendamentoRequest;
import net.backendted.guilhermeFerreira.dto.AgendamentoResponse;
import net.backendted.guilhermeFerreira.dto.StatusRequest;
import net.backendted.guilhermeFerreira.exception.ConflitoException;
import net.backendted.guilhermeFerreira.exception.RecursoNaoEncontradoException;
import net.backendted.guilhermeFerreira.model.*;
import net.backendted.guilhermeFerreira.repository.AgendamentoRepository;
import net.backendted.guilhermeFerreira.repository.ConvenioRepository;
import net.backendted.guilhermeFerreira.repository.PacienteRepository;
import net.backendted.guilhermeFerreira.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentos;
    private final PacienteRepository pacientes;
    private final ProfissionalRepository profissionais;
    private final ConvenioRepository convenios;

    public AgendamentoService(AgendamentoRepository agendamentos,
                              PacienteRepository pacientes,
                              ProfissionalRepository profissionais,
                              ConvenioRepository convenios) {
        this.agendamentos = agendamentos;
        this.pacientes = pacientes;
        this.profissionais = profissionais;
        this.convenios = convenios;
    }

    /**
     * ALGORITMO SELECTION SORT:
     * Ordena a lista de AgendamentoResponse por horário, paciente (A-Z) ou prioridade de status.
     */
    public List<AgendamentoResponse> selectionSort(List<AgendamentoResponse> lista, String criterio) {
        List<AgendamentoResponse> arr = new ArrayList<>(lista);
        int n = arr.size();

        Comparator<AgendamentoResponse> comp = (a, b) -> {
            if ("paciente".equalsIgnoreCase(criterio)) {
                int c = a.pacienteNome().compareToIgnoreCase(b.pacienteNome());
                return c != 0 ? c : a.horario().compareTo(b.horario());
            }
            if ("status".equalsIgnoreCase(criterio)) {
                int pA = StatusAgendamento.valueOf(a.status()).getPrioridade();
                int pB = StatusAgendamento.valueOf(b.status()).getPrioridade();
                if (pA != pB) return Integer.compare(pA, pB);
                int cData = a.data().compareTo(b.data());
                return cData != 0 ? cData : a.horario().compareTo(b.horario());
            }
            // Padrão: data e horário
            int cData = a.data().compareTo(b.data());
            if (cData != 0) return cData;
            int cHor = a.horario().compareTo(b.horario());
            return cHor != 0 ? cHor : a.id().compareTo(b.id());
        };

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (comp.compare(arr.get(j), arr.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                AgendamentoResponse temp = arr.get(i);
                arr.set(i, arr.get(minIdx));
                arr.set(minIdx, temp);
            }
        }
        return arr;
    }

    public List<AgendamentoResponse> listar(LocalDate data, String busca, String especialidade,
                                            String ordenarPor, Long pacienteId, Long profissionalId) {
        List<Agendamento> base = (data != null) ? agendamentos.findByData(data) : agendamentos.findAll();
        String termo = (busca != null) ? busca.trim().toLowerCase() : "";
        String espFiltro = (especialidade != null) ? especialidade.trim() : "";

        List<AgendamentoResponse> filtrados = new ArrayList<>();
        for (Agendamento a : base) {
            if (pacienteId != null && !a.getPaciente().getId().equals(pacienteId)) continue;
            if (profissionalId != null && !a.getProfissional().getId().equals(profissionalId)) continue;

            if (!espFiltro.isEmpty() && !a.getProfissional().getEspecialidade().equalsIgnoreCase(espFiltro)) {
                continue;
            }
            if (!termo.isEmpty()) {
                boolean match = a.getPaciente().getNome().toLowerCase().contains(termo)
                        || a.getProfissional().getNome().toLowerCase().contains(termo)
                        || a.getProfissional().getEspecialidade().toLowerCase().contains(termo);
                if (!match) continue;
            }
            filtrados.add(AgendamentoResponse.from(a));
        }

        return selectionSort(filtrados, ordenarPor);
    }

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest req) {
        if (req.profissionalId() == null || req.convenioId() == null || req.data() == null || req.horario() == null) {
            throw new IllegalArgumentException("Preencha profissional, convênio, data e horário.");
        }

        Profissional prof = profissionais.findById(req.profissionalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado."));

        if (prof.getStatus() != StatusProfissional.DISPONIVEL) {
            throw new ConflitoException("Este profissional está indisponível para agendamentos.");
        }

        Convenio conv = convenios.findById(req.convenioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Convênio não encontrado."));

        if (!conv.isAceito()) {
            throw new ConflitoException("Este convênio não é aceito pela clínica.");
        }

        boolean ocupado = agendamentos.existsByProfissionalIdAndDataAndHorarioAndStatusNot(
                prof.getId(), req.data(), req.horario(), StatusAgendamento.CANCELADO);
        if (ocupado) {
            throw new ConflitoException("Este profissional já possui uma consulta agendada nesse horário.");
        }

        Paciente pac;
        if (req.pacienteId() != null) {
            pac = pacientes.findById(req.pacienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado."));
        } else if (req.nomePaciente() != null && !req.nomePaciente().isBlank()) {
            pac = pacientes.findFirstByNomeIgnoreCase(req.nomePaciente().trim())
                    .orElseGet(() -> pacientes.save(new Paciente(
                            req.nomePaciente().trim(),
                            req.telefone() != null ? req.telefone().trim() : ""
                    )));
        } else {
            throw new IllegalArgumentException("Informe o paciente para o agendamento.");
        }

        TipoConsulta tipo = req.tipo() != null ? req.tipo() : TipoConsulta.PRIMEIRA_CONSULTA;
        Agendamento ag = new Agendamento(pac, prof, conv, req.data(), req.horario(), tipo, StatusAgendamento.AGUARDANDO);
        ag.setConfirmadoPaciente(true);
        return AgendamentoResponse.from(agendamentos.save(ag));
    }

    @Transactional
    public AgendamentoResponse confirmar(Long id, String perfil) {
        Agendamento ag = buscarEntidade(id);
        if (ag.getStatus() == StatusAgendamento.CANCELADO || ag.getStatus() == StatusAgendamento.ATENDIDO) {
            throw new ConflitoException("Não é possível confirmar um agendamento cancelado ou já atendido.");
        }
        if ("MEDICO".equalsIgnoreCase(perfil)) {
            ag.setConfirmadoMedico(true);
        } else {
            ag.setConfirmadoPaciente(true);
        }
        if (ag.getStatus() == StatusAgendamento.AGUARDANDO) {
            ag.setStatus(StatusAgendamento.CONFIRMADO);
        }
        return AgendamentoResponse.from(agendamentos.save(ag));
    }

    @Transactional
    public AgendamentoResponse iniciarAtendimento(Long id) {
        Agendamento ag = buscarEntidade(id);
        if (ag.getStatus() == StatusAgendamento.CANCELADO || ag.getStatus() == StatusAgendamento.ATENDIDO) {
            throw new ConflitoException("Este agendamento não pode entrar em atendimento.");
        }
        ag.setStatus(StatusAgendamento.EM_ATENDIMENTO);
        ag.setConfirmadoMedico(true);
        return AgendamentoResponse.from(agendamentos.save(ag));
    }

    @Transactional
    public AgendamentoResponse finalizarAtendimento(Long id, StatusRequest req) {
        Agendamento ag = buscarEntidade(id);
        if (ag.getStatus() == StatusAgendamento.CANCELADO) {
            throw new ConflitoException("Agendamento cancelado não pode ser atendido.");
        }
        ag.setStatus(StatusAgendamento.ATENDIDO);
        ag.setConfirmadoMedico(true);
        if (req != null) {
            ag.setQueixa(req.queixa() != null ? req.queixa().trim() : "");
            ag.setDiagnostico(req.diagnostico() != null ? req.diagnostico().trim() : "");
            ag.setPrescricao(req.prescricao() != null ? req.prescricao().trim() : "");
        }
        ag.setAtendidoEm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        return AgendamentoResponse.from(agendamentos.save(ag));
    }

    @Transactional
    public AgendamentoResponse cancelar(Long id) {
        Agendamento ag = buscarEntidade(id);
        ag.setStatus(StatusAgendamento.CANCELADO);
        return AgendamentoResponse.from(agendamentos.save(ag));
    }

    @Transactional
    public AgendamentoResponse atualizarStatus(Long id, StatusRequest req) {
        if (req == null || req.status() == null) {
            throw new IllegalArgumentException("Informe o novo status.");
        }
        return switch (req.status()) {
            case CONFIRMADO -> confirmar(id, req.perfil());
            case EM_ATENDIMENTO -> iniciarAtendimento(id);
            case ATENDIDO -> finalizarAtendimento(id, req);
            case CANCELADO -> cancelar(id);
            default -> {
                Agendamento ag = buscarEntidade(id);
                ag.setStatus(req.status());
                yield AgendamentoResponse.from(agendamentos.save(ag));
            }
        };
    }

    private Agendamento buscarEntidade(Long id) {
        return agendamentos.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado."));
    }
}
