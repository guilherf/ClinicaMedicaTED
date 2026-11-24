package net.backendted.guilhermeFerreira.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "convenio_id")
    private Convenio convenio;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConsulta tipo = TipoConsulta.PRIMEIRA_CONSULTA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status = StatusAgendamento.AGUARDANDO;

    @Column(nullable = false)
    private boolean confirmadoPaciente = false;

    @Column(nullable = false)
    private boolean confirmadoMedico = false;

    @Column(length = 1000)
    private String queixa = "";

    @Column(length = 1000)
    private String diagnostico = "";

    @Column(length = 1000)
    private String prescricao = "";

    private String atendidoEm = "";

    public Agendamento() {}

    public Agendamento(Paciente paciente, Profissional profissional, Convenio convenio,
                       LocalDate data, LocalTime horario, TipoConsulta tipo, StatusAgendamento status) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.convenio = convenio;
        this.data = data;
        this.horario = horario;
        this.tipo = tipo;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }
    public Convenio getConvenio() { return convenio; }
    public void setConvenio(Convenio convenio) { this.convenio = convenio; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }
    public TipoConsulta getTipo() { return tipo; }
    public void setTipo(TipoConsulta tipo) { this.tipo = tipo; }
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public boolean isConfirmadoPaciente() { return confirmadoPaciente; }
    public void setConfirmadoPaciente(boolean confirmadoPaciente) { this.confirmadoPaciente = confirmadoPaciente; }
    public boolean isConfirmadoMedico() { return confirmadoMedico; }
    public void setConfirmadoMedico(boolean confirmadoMedico) { this.confirmadoMedico = confirmadoMedico; }
    public String getQueixa() { return queixa; }
    public void setQueixa(String queixa) { this.queixa = queixa; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getPrescricao() { return prescricao; }
    public void setPrescricao(String prescricao) { this.prescricao = prescricao; }
    public String getAtendidoEm() { return atendidoEm; }
    public void setAtendidoEm(String atendidoEm) { this.atendidoEm = atendidoEm; }
}
