package net.backendted.guilhermeFerreira.config;


import net.backendted.guilhermeFerreira.model.*;
import net.backendted.guilhermeFerreira.repository.AgendamentoRepository;
import net.backendted.guilhermeFerreira.repository.ConvenioRepository;
import net.backendted.guilhermeFerreira.repository.PacienteRepository;
import net.backendted.guilhermeFerreira.repository.ProfissionalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DadosIniciais implements CommandLineRunner {

    private final ProfissionalRepository profissionais;
    private final ConvenioRepository convenios;
    private final PacienteRepository pacientes;
    private final AgendamentoRepository agendamentos;

    public DadosIniciais(ProfissionalRepository profissionais, ConvenioRepository convenios,
                         PacienteRepository pacientes, AgendamentoRepository agendamentos) {
        this.profissionais = profissionais;
        this.convenios = convenios;
        this.pacientes = pacientes;
        this.agendamentos = agendamentos;
    }

    @Override
    public void run(String... args) {
        if (profissionais.count() > 0) {
            return;
        }

        Profissional camila = profissionais.save(new Profissional("Dra. Camila Torres", "Cardiologia", "CRM 3390", "camila@clinica.com", "123456"));
        Profissional lucas = profissionais.save(new Profissional("Dr. Lucas Brandão", "Pediatria", "CRM 7319", "lucas@clinica.com", "123456"));
        Profissional helena = profissionais.save(new Profissional("Dra. Helena Ribeiro", "Neurologia", "CRM 2564", "helena@clinica.com", "123456"));
        Profissional rafael = profissionais.save(new Profissional("Dr. Rafael Nunes", "Clínico geral", "CRM 5128", "rafael@clinica.com", "123456"));
        Profissional marina = profissionais.save(new Profissional("Dra. Marina Azevedo", "Oncologia", "CRM 4821", "marina@clinica.com", "123456"));

        Convenio particular = convenios.save(new Convenio("Particular", "Atendimento sem convênio", true));
        Convenio vidaPlena = convenios.save(new Convenio("Vida Plena", "Convênio credenciado", true));
        Convenio bemSaude = convenios.save(new Convenio("Bem Saúde", "Convênio credenciado", true));

        LocalDate hoje = LocalDate.now();
        agendar(hoje, "08:00", "Eduardo Martins", "(11) 98742-3019", "eduardo@paciente.com", "123456", camila, vidaPlena, TipoConsulta.PRIMEIRA_CONSULTA, StatusAgendamento.CONFIRMADO);
        agendar(hoje, "09:30", "Sofia Almeida", "(11) 99615-7742", "sofia@paciente.com", "123456", lucas, bemSaude, TipoConsulta.RETORNO, StatusAgendamento.AGUARDANDO);
        agendar(hoje, "10:15", "João Pedro Lima", "(11) 97522-1180", "joao@paciente.com", "123456", helena, particular, TipoConsulta.PRIMEIRA_CONSULTA, StatusAgendamento.CONFIRMADO);
        agendar(hoje, "11:00", "Márcia de Souza", "(11) 98133-6425", "marcia@paciente.com", "123456", rafael, vidaPlena, TipoConsulta.RETORNO, StatusAgendamento.EM_ATENDIMENTO);
        agendar(hoje, "14:00", "Ana Clara Mendes", "(11) 99208-4317", "ana@paciente.com", "123456", marina, bemSaude, TipoConsulta.RETORNO, StatusAgendamento.CONFIRMADO);
        agendar(hoje, "15:30", "Carlos Henrique Reis", "(11) 98854-1096", "carlos@paciente.com", "123456", camila, particular, TipoConsulta.PRIMEIRA_CONSULTA, StatusAgendamento.AGUARDANDO);
    }

    private void agendar(LocalDate data, String hora, String nome, String telefone, String email, String senha,
                         Profissional profissional, Convenio convenio, TipoConsulta tipo, StatusAgendamento status) {
        Paciente paciente = pacientes.save(new Paciente(nome, telefone, email, senha));
        Agendamento ag = new Agendamento(paciente, profissional, convenio, data, LocalTime.parse(hora), tipo, status);
        if (status == StatusAgendamento.CONFIRMADO || status == StatusAgendamento.EM_ATENDIMENTO) {
            ag.setConfirmadoPaciente(true);
            ag.setConfirmadoMedico(true);
        }
        agendamentos.save(ag);
    }
}
